package sansan.sentix.common.Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import sansan.sentix.Entity.Config;
import sansan.sentix.common.Exception.ErrorCode;
import sansan.sentix.common.Exception.SentixException;
import sansan.sentix.Repository.ConfigRepository;
import sansan.sentix.Module.Market.Repository.SectorRepository;
import sansan.sentix.common.Utils.Constants;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConfigCache {

    private static RedisTemplate<String, String> redisTemplate;
    private static ConfigRepository configRepository;
    private static SectorRepository sectorRepository;

    @Autowired
    public ConfigCache(RedisTemplate<String, String> redisTemplate,
                       SectorRepository sectorRepository,
                       ConfigRepository configRepository) {
        ConfigCache.redisTemplate = redisTemplate;
        ConfigCache.configRepository = configRepository;
        ConfigCache.sectorRepository = sectorRepository;
    }

    private static final String PREFIX = "config:";

    public static String getConfig(String key) {
        try {
            String redisKey = PREFIX + key;

            String value = redisTemplate.opsForValue().get(redisKey);

            if (value != null) {
                return value;
            }
            switch (key) {
                case Constants.TICKER_LIST:
                    Clob result = sectorRepository.concatAllIndustryCodes();
                    log.info("concatAllSector value class = {}", result == null ? "null" : result.length());
                    if (result == null) {
                        value = "";
                        break;
                    }
                    value = result.getSubString(1, (int) result.length());
                    break;
                default:
                    value = configRepository.findByConfigKey(key).orElseThrow(() -> {
                        log.error("ConfigCache::getConfig not found config key {}", key);
                        return new SentixException(ErrorCode.BUSINESS_ERROR);
                    }).getConfigValue();
            }
            redisTemplate.opsForValue().set(redisKey, value, 1, TimeUnit.MINUTES);
            return value;

        } catch (SQLException e) {
            log.error("ConfigCache.getConfig failed (SQL). key={}", key, e);
            throw new SentixException(ErrorCode.BUSINESS_ERROR); // wrap thành unchecked
        } catch (Exception e) {
            log.error("ConfigCache.getConfig failed. key={}", key, e);
            throw e;
        }
    }

    public static void putAll(List<Config> configs) {
        Map<String, String> values = configs.stream().collect(Collectors.toMap(config -> PREFIX + config.getConfigKey(), Config::getConfigValue));
        redisTemplate.opsForValue().multiSet(values);
        values.keySet().forEach(key -> redisTemplate.expire(key, 1, TimeUnit.MINUTES));
    }

}
