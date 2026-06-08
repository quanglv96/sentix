package sansan.sentix.Module.Articles.Factory.CrawlNews;

import sansan.sentix.Module.Articles.Request.ArticlesRawMessage;
import sansan.sentix.Module.Articles.Utils.SourceTypeArticles;

import java.util.List;

public interface CrawlNewsService {
    SourceTypeArticles getSourceType();
    // Hàm thực thi quét tin thô từ nguồn
    List<ArticlesRawMessage> crawlLatestNews();

    String getRawContent(String urlSource);
}
