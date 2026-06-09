package sansan.sentix.Module.AiIntegration.Service;

import sansan.sentix.Common.Request.ArticleSentimentRequest;
import sansan.sentix.Common.Response.ArticleSentimentResponse;
import sansan.sentix.Module.AiIntegration.Utils.AiEnum;

public interface AiService {
    AiEnum getStrategy();

    ArticleSentimentResponse analyzeArticleSentiment(ArticleSentimentRequest request);
}
