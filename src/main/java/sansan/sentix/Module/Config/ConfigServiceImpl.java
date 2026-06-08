package sansan.sentix.Module.Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import sansan.sentix.Module.Config.Entity.Config;
import sansan.sentix.Module.Config.Repository.ConfigRepository;
import sansan.sentix.Common.Cache.GlobalCache;
import sansan.sentix.Common.Exception.ErrorCode;
import sansan.sentix.Common.Exception.SentixException;
import sansan.sentix.Module.Config.Utils.ConfigStatus;
import sansan.sentix.Common.Utils.Constants;

import java.util.List;


@Service
@Log4j2
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {
    private final ConfigRepository configRepository;

    private final RestTemplate restTemplate;

    public String findByConfigKey(String key) {
        return configRepository.findByConfigKey(key)
                .orElseThrow(() -> {
                    log.error("Config not found. key={}", key);
                    return new SentixException(ErrorCode.BUSINESS_ERROR);
                })
                .getConfigValue();
    }

    @Override
    public void loadMemoryConfig() {
        List<Config> configs = configRepository.findAllByStatus(ConfigStatus.ACTIVE);
        if (!CollectionUtils.isEmpty(configs)) {
            GlobalCache.putAll(configs);
            log.info("Loaded {} configs into cache", configs.size());
//            webhookTelegram();
        } else {
            log.error("ERROR-LoadMemoryConfig: No configuration found in the database.");
            throw new SentixException(ErrorCode.DATABASE_ERROR);
        }
    }

    private void webhookTelegram() {
        String urlRename = GlobalCache.getConfig(Constants.TELEGRAM_SYNTAX_WEB_HOOK)
                .replace(Constants.BOT_TOKEN, GlobalCache.getConfig(Constants.TELEGRAM_TOKEN_ADMIN)
                        .replace(Constants.URL_WEBHOOK, GlobalCache.getConfig(Constants.TELEGRAM_URL_RECEIVE_ADMIN)));

        ResponseEntity<String> responseRename = restTemplate.exchange(urlRename, HttpMethod.POST, null, String.class);
        if (responseRename.getStatusCode().isError()) {
            log.error("Failed to send message to Telegram: {}", responseRename.getBody());
        }
        log.info("Telegram response: {}", responseRename.getBody());
    }
}
