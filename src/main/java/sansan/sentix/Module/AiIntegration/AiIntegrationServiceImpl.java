package sansan.sentix.Module.AiIntegration;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import sansan.sentix.Common.Request.ArticleSentimentRequest;
import sansan.sentix.Common.Response.ArticleSentimentResponse;
import sansan.sentix.Module.AiIntegration.Factory.AiFactory;
import sansan.sentix.Module.AiIntegration.Service.AiService;

import javax.annotation.Resource;

@Service
@Log4j2
public class AiIntegrationServiceImpl implements AiIntegrationService {
    @Resource
    private AiFactory aiFactory;

    @Override
    public ArticleSentimentResponse analyzeArticleSentiment(ArticleSentimentRequest request) {
        AiService service = aiFactory.getService();
        return service.analyzeArticleSentiment(request);
    }
}
