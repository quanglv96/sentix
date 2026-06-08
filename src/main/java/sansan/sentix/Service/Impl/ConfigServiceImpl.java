package sansan.sentix.Service.Impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import sansan.sentix.common.Config.ConfigCache;
import sansan.sentix.Entity.Config;
import sansan.sentix.common.Exception.ErrorCode;
import sansan.sentix.common.Exception.SentixException;
import sansan.sentix.Repository.ConfigRepository;
import sansan.sentix.Service.ConfigService;
import sansan.sentix.common.Utils.ConfigStatus;
import sansan.sentix.common.Utils.Constants;

import javax.annotation.Resource;
import java.util.List;

@Service
@Log4j2
public class ConfigServiceImpl implements ConfigService {
    @Resource
    private ConfigRepository configRepository;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public void loadMemoryConfig() {
        List<Config> configs = configRepository.findAllByStatus(ConfigStatus.ACTIVE);
        if (!CollectionUtils.isEmpty(configs)) {
            ConfigCache.putAll(configs);
            log.info("Loaded {} configs into Redis cache", configs.size());
//            webhookTelegram();
        } else {
            log.error("ERROR-LoadMemoryConfig: No configuration found in the database.");
            throw new SentixException(ErrorCode.DATABASE_ERROR);
        }
    }

    private void webhookTelegram() {
        String urlRename = ConfigCache.getConfig(Constants.TELEGRAM_SYNTAX_WEB_HOOK)
                .replace(Constants.BOT_TOKEN, ConfigCache.getConfig(Constants.TELEGRAM_TOKEN_ADMIN)
                        .replace(Constants.URL_WEBHOOK, ConfigCache.getConfig(Constants.TELEGRAM_URL_RECEIVE_ADMIN)));

        ResponseEntity<String> responseRename = restTemplate.exchange(urlRename, HttpMethod.POST, null, String.class);
        if (responseRename.getStatusCode().isError()) {
            log.error("Failed to send message to Telegram: {}", responseRename.getBody());
        }
        log.info("Telegram response: {}", responseRename.getBody());
    }
}
