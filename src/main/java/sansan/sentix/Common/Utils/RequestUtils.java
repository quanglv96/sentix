package sansan.sentix.Common.Utils;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class RequestUtils {
    public static String getHeader(String headerName) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return getHeader(headerName, request);
        }
        return null;
    }

    public static String getToken() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String auth = getHeader("Authorization", request);
            return StringUtils.isBlank(auth) ? null : auth.replace("Bearer ", "");
        }
        return null;
    }

    public static HttpServletRequest httpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }

    private static String getHeader(String headerName, HttpServletRequest request) {
        if (headerName.equalsIgnoreCase(Constants.X_FORWARDED_HOST)
                || headerName.equalsIgnoreCase(Constants.HOST)) {
            return getHostServer(request);
        }
        String stringHeader = request.getHeader(headerName);
        if (StringUtils.isEmpty(stringHeader)) {
            stringHeader = request.getHeader(headerName.replace("_", "-"));
        }
        return stringHeader;
    }

    public static String getClientIp() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return getClientIp(request);
        }
        return null;
    }

    public static String getSessionId() {
        return getHeader(Constants.SESSION_ID);
    }

    public static String getUsername() {
        return getHeader(Constants.USERNAME);
    }

    public static String getUserId() {
        return getHeader(Constants.USER_ID);
    }

    public static String getSessionId(HttpServletRequest request) {
        return getHeader(Constants.SESSION_ID, request);
    }

    public static String getDeviceId() {
        return getHeader(Constants.USER_AGENT);
    }

    public static String getDeviceId(HttpServletRequest request) {
        return getHeader(Constants.USER_AGENT, request);
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * Lấy host server chính xác, ưu tiên X-Forwarded-Host nếu có.
     */
    public static String getHostServer(HttpServletRequest request) {
        String host = request.getHeader(Constants.X_FORWARDED_HOST);
        if (StringUtils.isEmpty(host)) {
            host = request.getHeader(Constants.HOST);
        }
        if (StringUtils.isEmpty(host)) {
            host = request.getServerName(); // fallback localhost / IP
        }
        return host;
    }


    public static String getRequestURI() {
        HttpServletRequest httpServletRequest = httpServletRequest();
        if (ObjectUtils.isEmpty(httpServletRequest)) {
            return null;
        }
        return httpServletRequest.getRequestURI();
    }
}
