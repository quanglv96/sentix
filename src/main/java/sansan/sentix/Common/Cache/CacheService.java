package sansan.sentix.Common.Cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import sansan.sentix.Module.Config.ConfigService;
import sansan.sentix.Module.Config.Entity.Config;
import sansan.sentix.Module.Market.MarketService;
import sansan.sentix.Common.Exception.ErrorCode;
import sansan.sentix.Common.Exception.SentixException;
import sansan.sentix.Common.Utils.Constants;

import java.sql.Clob;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
@Log4j2
public class CacheService {
    private final ConfigService configService;
    private final MarketService marketService;

    @Cacheable(value = "config", key = "#key")
    public String getConfig(String key) {
        log.info("Cache miss. Loading config: {}", key);
        try {
            switch (key) {

                case Constants.TICKER_LIST:
                    Clob result = marketService.concatAllIndustryCodes();

                    log.info("Load ticker list from DB");

                    if (result == null) {
                        return "";
                    }

                    return result.getSubString(1, (int) result.length());

                default:
                    return configService.findByConfigKey(key);
            }
        } catch (SQLException e) {
            log.error("Load config failed. key={}", key, e);
            throw new SentixException(ErrorCode.BUSINESS_ERROR);
        }
    }

    @CachePut(value = "config", key = "#config.configKey")
    public String putConfig(Config config) {
        return config.getConfigValue();
    }

}