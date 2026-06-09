package sansan.sentix.Module.Articles;

import sansan.sentix.Module.Articles.Entity.ArticlesRawEntity;

import java.util.List;

public interface ArticlesService {
    void analyzeNewsRaw(Long idArticlesRaw);

    List<ArticlesRawEntity> crawlLatestNews();

    boolean analyzeArticleSentiment(ArticlesRawEntity raw);
}
