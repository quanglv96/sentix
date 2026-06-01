package sansan.sentix.Service.Impl;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import sansan.sentix.Config.SystemConfig;
import sansan.sentix.Entity.Config;
import sansan.sentix.Exception.SentixException;
import sansan.sentix.Repository.ConfigRepository;
import sansan.sentix.Service.ConfigService;
import sansan.sentix.Utils.AppStatus;
import sansan.sentix.Utils.CommonUtils;
import sansan.sentix.Utils.KeyConfig;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServiceImpl.class);

    @Resource
    private ConfigRepository configRepository;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public void loadMemoryConfig() {
        List<Config> configs = configRepository.findAllByStatus(AppStatus.ACTIVE.getStatus());
        Collections.shuffle(configs); // trộn để random prompt hugging face
        if (!CollectionUtils.isEmpty(configs)) {
            for (Config config : configs) {
                if (ObjectUtils.isEmpty(config.getType())) {
                    LOGGER.error("Config error: {}", CommonUtils.convertObjectToJson(config));
                    throw new SentixException("Config error:", CommonUtils.convertObjectToJson(config));
                }
                switch (config.getType()) {
                    case "AUTH":
                        SystemConfig.AUTH.put(config.getKey(), config.getValue());
                        break;
                    case "SYSTEM":
                        SystemConfig.SYSTEM.put(config.getKey(), config.getValue());
                        break;
                    case "TELEGRAM":
                        SystemConfig.TELEGRAM.put(config.getKey(), config.getValue());
                        break;
                    default:
                        // Handle unknown type
                        break;
                }
            }
//            webhookTelegram();
        } else {
            LOGGER.error("ERROR-LoadMemoryConfig: No configuration found in the database.");
            throw new SentixException("ERROR-LoadMemoryConfig: No configuration found in the database.");
        }
    }

    private void webhookTelegram() {
        String urlRename = SystemConfig.TELEGRAM.get(KeyConfig.CONFIG_WEBHOOK).replace(KeyConfig.BOT_TOKEN, SystemConfig.TELEGRAM.get(KeyConfig.API_TOKEN)).replace(KeyConfig.URL_WEBHOOK, SystemConfig.TELEGRAM.get(KeyConfig.URL_RECEIVE));

        ResponseEntity<String> responseRename = restTemplate.exchange(urlRename, HttpMethod.POST, null, String.class);
        if (responseRename.getStatusCode().isError()) {
            LOGGER.error("Failed to send message to Telegram: {}", responseRename.getBody());
        }
        LOGGER.info("Telegram response: {}", responseRename.getBody());

    }
}
