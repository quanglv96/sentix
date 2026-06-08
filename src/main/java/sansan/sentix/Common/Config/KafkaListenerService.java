package sansan.sentix.Common.Config;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import sansan.sentix.Module.Articles.Service.ArticlesRawService;
import sansan.sentix.Common.Utils.Constants;

import javax.annotation.Resource;

@Component
@Log4j2
public class KafkaListenerService {
    @Resource
    private ArticlesRawService articlesRawService;

    @KafkaListener(topics = Constants.TOPIC_ANALYSIS_NEWS, containerFactory = "analysisNewsKafkaListenerContainerFactory")
    public void analysisNews(String message, Acknowledgment ack) {
        log.info("KafkaListenerService::analysisNews topic {} message: {}", Constants.TOPIC_ANALYSIS_NEWS, message);
        try {
            articlesRawService.analyzeNewsRaw(Long.valueOf(message));
        } catch (Exception e) {
            log.error("KafkaListenerService::analysisNews error: {}", e.getMessage());
        } finally {
            ack.acknowledge();
        }
    }

}
