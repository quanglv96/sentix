package sansan.sentix.Service.Impl;

import org.apache.commons.lang3.ObjectUtils;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sansan.sentix.Config.ConfigCache;
import sansan.sentix.Service.RedisService;
import sansan.sentix.Utils.Constants;
import sansan.sentix.Utils.DateTimeUtils;
import sansan.sentix.Utils.RequestUtils;

import javax.annotation.Resource;
import java.time.Duration;

@Service
public class RedisRateLimiterService {

    Logger logger = LoggerFactory.getLogger(RedisRateLimiterService.class);
    @Resource
    private RedissonClient redisson;

    @Resource
    private RedisService redisService;

    private static Long MAX_REQUESTS = 50L;        // Số request tối đa
    private static Long WINDOW_SECOND = 100L; // Trong 10 giây

    public boolean allowRequest(String uri) {
        logger.info("Rate limiting check for URI: {}", uri);
        if (uri.contains("/facebook/webhook") || uri.contains("/api/admin/")) {
            return true;
        }
        String ip = RequestUtils.getClientIp();
        if (ObjectUtils.isEmpty(ip) || redisService.isBlock(ip)) {
            logger.error("Rate limiting check for URI: {}", uri);
            return false;
        }
        if (ObjectUtils.isEmpty(MAX_REQUESTS)
                || ObjectUtils.isEmpty(WINDOW_SECOND)
                || !MAX_REQUESTS.toString().equalsIgnoreCase(ConfigCache.getConfig(Constants.RATE_LIMIT_MAX_REQ))
                || !WINDOW_SECOND.toString().equalsIgnoreCase(ConfigCache.getConfig(Constants.RATE_LIMIT_WINDOW_SECONDS))) {
            MAX_REQUESTS = Long.valueOf(ConfigCache.getConfig((Constants.RATE_LIMIT_MAX_REQ)));        // Số request tối đa
            WINDOW_SECOND = Long.parseLong(ConfigCache.getConfig((Constants.RATE_LIMIT_WINDOW_SECONDS))); // Trong 10 giây
        }
        if (!checkLimit(ip)) {
            redisService.putBlockIp(ip, DateTimeUtils.nowLocalDateTime().format(Constants.DATE_TIME_FORMATTER_1));
            return false;
        }
        return true;
    }

    private boolean checkLimit(String ip) {
        String key = "rate-limit:" + ip;
        // Tăng counter atomic
        long current = redisson.getAtomicLong(key).incrementAndGet();
        if (current == 1) {
            // Lần đầu tạo key, set TTL
            redisson.getBucket(key).expire(Duration.ofSeconds(WINDOW_SECOND));
        }

        if (current > MAX_REQUESTS) {
            logger.error("IP {} bị chặn do vượt giới hạn rate-limit tại {}", ip,
                    DateTimeUtils.nowLocalDateTime().format(Constants.DATE_TIME_FORMATTER_1));
            redisService.putBlockIp(ip,
                    DateTimeUtils.nowLocalDateTime().format(Constants.DATE_TIME_FORMATTER_1));
            return false;
        }
        return true;
    }
}