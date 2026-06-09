package sansan.sentix.Module.AiIntegration;

import sansan.sentix.Common.Request.ArticleSentimentRequest;
import sansan.sentix.Common.Response.ArticleSentimentResponse;

public interface AiIntegrationService {
    ArticleSentimentResponse analyzeArticleSentiment(ArticleSentimentRequest request);
}
