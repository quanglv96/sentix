package sansan.sentix.Security;

import io.sentry.Sentry;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sansan.sentix.common.Exception.ErrorCode;
import sansan.sentix.common.Exception.SentixException;
import sansan.sentix.Service.Impl.RedisRateLimiterService;
import sansan.sentix.common.Utils.Constants;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Resource
    private TokenProvider tokenProvider;

    @Resource
    private RedisRateLimiterService rateLimiterService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String traceId = request.getHeader("X-Trace-Id");

        if (StringUtils.isBlank(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        try {

            MDC.put("traceId", traceId);

            response.setHeader("X-Trace-Id", traceId);

            Sentry.setTag("traceId", traceId);

            String finalTraceId = traceId;
            Sentry.configureScope(scope -> {
                scope.setTag("traceId", finalTraceId);
                scope.setExtra("method", request.getMethod());
                scope.setExtra("url", request.getRequestURI());
                scope.setExtra("queryString", request.getQueryString());
                scope.setExtra("clientIp", request.getRemoteAddr());
            });

            logger.info(
                    "REQUEST_INFO traceId={} method={} uri={} ip={} userAgent={}",
                    traceId,
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getRemoteAddr(),
                    request.getHeader("User-Agent")
            );

            verifyRequest(request, response, filterChain);

        } catch (Exception ex) {

            logger.error("REQUEST ERROR traceId={}", traceId, ex);

            Sentry.captureException(ex);

            throw ex;

        } finally {
            SecurityContextHolder.clearContext();
            MDC.clear();
        }
    }

    protected void verifyRequest(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        if (!rateLimiterService.allowRequest(url)) {
            response.setStatus(429);
            response.getWriter().write("Too many requests");
            return;
        }
        String token = request.getHeader(Constants.AUTHORIZATION);
        if (StringUtils.isNotEmpty(token)) {
            token = token.replace("Bearer ", "");
            if (tokenProvider.verifyAccessToken(token)) {
                throw new SentixException(ErrorCode.FORBIDDEN);
            }        }
        filterChain.doFilter(request, response);
    }
}

