package sansan.sentix.Module.Articles;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sansan.sentix.Common.Request.ArticleSentimentRequest;
import sansan.sentix.Common.Response.ArticleSentimentResponse;
import sansan.sentix.Module.AiIntegration.AiIntegrationService;
import sansan.sentix.Module.Articles.Entity.ArticleHashEntity;
import sansan.sentix.Module.Articles.Entity.ArticlesRawEntity;
import sansan.sentix.Module.Articles.Entity.CrawlTargetEntity;
import sansan.sentix.Module.Articles.Factory.CrawlNews.CrawlNewsFactory;
import sansan.sentix.Module.Articles.Repository.ArticleHashRepository;
import sansan.sentix.Module.Articles.Repository.ArticleRawRepository;
import sansan.sentix.Module.Articles.Repository.CrawlTargetRepository;
import sansan.sentix.Module.Articles.Service.ArticlesRawService;
import sansan.sentix.Module.Articles.Request.ArticlesRawMessage;
import sansan.sentix.Common.Config.AsyncConfig;
import sansan.sentix.Module.Articles.Utils.ArticlesRawStatus;
import sansan.sentix.Common.Utils.DateTimeUtils;
import sansan.sentix.Common.Utils.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ArticlesServiceImpl implements ArticlesService {
    @Resource
    private ArticlesRawService articlesRawService;

    @Resource
    private ArticleRawRepository articleRawRepository;

    @Resource
    private ArticleHashRepository articleHashRepository;

    @Resource
    private CrawlNewsFactory crawlNewsFactory;

    @Resource
    private CrawlTargetRepository crawlTargetRepository;

    @Resource
    private AiIntegrationService aiIntegrationService;

    @Resource
    @Qualifier(value = "crawlNews")
    private AsyncConfig asyncConfig;

    @Override
    public void analyzeNewsRaw(Long idArticlesRaw) {
        articlesRawService.analyzeNewsRaw(idArticlesRaw);
    }

    @Override
    public List<ArticlesRawEntity> crawlLatestNews() {
        List<CrawlTargetEntity> targets = crawlTargetRepository.findAllByStatus(ArticlesRawStatus.ACTIVE);
        if (targets.isEmpty()) {
            //TODO: thông báo admin
            return new ArrayList<>();
        }
        // 2. Kích hoạt quét ĐỒNG THỜI toàn bộ danh sách URL bằng CompletableFuture
        List<CompletableFuture<List<ArticlesRawMessage>>> futures = targets.stream()
                .map(target -> CompletableFuture.supplyAsync(() -> {
                    try {
                        // Gọi Factory lấy chiến lược bóc tách phù hợp (Ví dụ: CAFEF)
                        var crawler = crawlNewsFactory.getService(target.getSourceType());
                        return crawler.crawlLatestNews();
                    } catch (Exception e) {
                        log.error("Crawling failed target {} error mess: {}", target.getId(), e.getMessage());
                        return List.<ArticlesRawMessage>of();
                    }
                }, asyncConfig.crawlNewsExecutor()))
                .collect(Collectors.toList());

        // 3. Chờ tất cả các luồng cào xong và gom kết quả về một mối
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).orTimeout(60, TimeUnit.SECONDS)
                .join();
        List<ArticlesRawMessage> allNewArticles =
                futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

        log.info("Total article found={}", allNewArticles.size());

        List<ArticlesRawEntity> raw = new ArrayList<>();
        // lọc trùng
        for (ArticlesRawMessage article : allNewArticles) {
            String hashTitle = StringUtils.sha256Hex(article.getTitle());
            long itExists = articleRawRepository.existsByTitleHashAndIdPublishAndSourceType(hashTitle, article.getIdPublish(), article.getSourceType().getValue());
            if (itExists > 0) {
                continue;
            }
            ArticlesRawEntity rawEntity = new ArticlesRawEntity();
            rawEntity.setIdPublish(article.getIdPublish());
            rawEntity.setTitle(article.getTitle());
            rawEntity.setSourceUrl(article.getSourceUrl());
            rawEntity.setSourceType(article.getSourceType());
            rawEntity.setPublishedAt(DateTimeUtils.nowLocalDateTime());
            rawEntity.setStatus(ArticlesRawStatus.PENDING);
            rawEntity.setCreatedAt(DateTimeUtils.nowLocalDateTime());

            rawEntity = articleRawRepository.save(rawEntity);
            ArticleHashEntity hashEntity = new ArticleHashEntity();
            hashEntity.setTitleHash(hashTitle);
            hashEntity.setArticleId(rawEntity.getId());
            hashEntity.setCreatedAt(DateTimeUtils.nowLocalDateTime());
            articleHashRepository.save(hashEntity);

            raw.add(rawEntity);
        }
        return raw;
    }

    @Override
    public boolean analyzeArticleSentiment(ArticlesRawEntity raw) {
        ArticleSentimentRequest request = ArticleSentimentRequest.builder()
                .title(raw.getTitle())
                .content(raw.getRawContent())
                .build();
        ArticleSentimentResponse response = aiIntegrationService.analyzeArticleSentiment(request);
        return false;
    }
}
