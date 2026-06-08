package sansan.sentix.Factory.CrawlNews;

import sansan.sentix.Request.ArticlesRawMessage;
import sansan.sentix.common.Utils.SourceTypeArticles;

import java.util.List;

public interface CrawlNewsService {
    SourceTypeArticles getSourceType();
    // Hàm thực thi quét tin thô từ nguồn
    List<ArticlesRawMessage> crawlLatestNews();
    String getRawContent(String urlSource);
}
