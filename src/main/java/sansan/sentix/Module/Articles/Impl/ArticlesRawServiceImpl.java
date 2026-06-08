package sansan.sentix.Module.Articles.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sansan.sentix.common.Config.ConfigCache;
import sansan.sentix.common.Exception.ErrorCode;
import sansan.sentix.common.Exception.SentixException;
import sansan.sentix.Factory.CrawlNews.CrawlNewsFactory;
import sansan.sentix.Factory.CrawlNews.CrawlNewsService;
import sansan.sentix.Module.Articles.Entity.ArticlesRawEntity;
import sansan.sentix.Module.Articles.Repository.ArticleRawRepository;
import sansan.sentix.Module.Articles.Service.ArticlesRawService;
import sansan.sentix.common.Utils.ArticlesRawStatus;
import sansan.sentix.common.Utils.Constants;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticlesRawServiceImpl implements ArticlesRawService {
    @Resource
    private ArticleRawRepository articleRawRepository;
    @Resource
    private CrawlNewsFactory crawlNewsFactory;
    private final Pattern TICKER_PATTERN = Pattern.compile("\\b[A-Z]{2,5}\\b");
    // volatile: Khi một thread thay đổi giá trị biến, các thread khác sẽ nhìn thấy giá trị mới ngay lập tức.
    private static volatile String lastTickerConfig;
    private static volatile Set<String> tickerSet;

    @Override
    public void analyzeNewsRaw(Long idArticlesRaw) {
        ArticlesRawEntity rawEntity = articleRawRepository.findByIdAndStatus(idArticlesRaw, ArticlesRawStatus.PENDING);
        if (rawEntity == null) {
            log.error("Article not found. idArticlesRaw={}", idArticlesRaw);
            return;
        }
        try {
            log.info("Processing article. id={}, sourceType={}, title={}", rawEntity.getId(), rawEntity.getSourceType(), rawEntity.getTitle());
            // filter content
            CrawlNewsService service = crawlNewsFactory.getService(rawEntity.getSourceType());
            String rawContent = service.getRawContent(rawEntity.getSourceUrl());
            if (rawContent == null) {
                log.error("CrawlNewsFactory:: getRawContent returned null with idArticlesRaw: {}", idArticlesRaw);
                throw new SentixException(ErrorCode.BUSINESS_ERROR);
            }

            boolean isIrrelevantArticle = isIrrelevantArticle(rawEntity.getTitle(), rawContent);
            log.info("Article filtered result. id={}, isIrrelevant={}", rawEntity.getId(), isIrrelevantArticle);
            if (isIrrelevantArticle) {
                rawEntity.setStatus(ArticlesRawStatus.INACTIVE);
            } else {
                rawEntity.setRawContent(rawContent);
                rawEntity.setStatus(ArticlesRawStatus.ACTIVE);
                // 🔥 BỔ SUNG 3: Trích xuất danh sách Ticker từ tiêu đề đối với bài viết hợp lệ
                List<String> detectedTickers = extractTickers(rawEntity.getTitle());
                if (!detectedTickers.isEmpty()) {
                    log.info("Extracted tickers. id={}, tickers={}", rawEntity.getId(), detectedTickers);
                    // Chuyển List thành chuỗi dạng "HPG;SSI" để chuẩn bị lưu DB hoặc xử lý bắn Kafka gửi đi
                    String tickersStr = String.join(";", detectedTickers);
                    rawEntity.setTicker(tickersStr); // Hãy đảm bảo thuộc tính này có sẵn trong Entity của bạn
                    log.info("🎯 Phát hiện các mã cổ phiếu trong tiêu đề: {}", tickersStr);
                }
            }
        } catch (Exception e) {
            log.error("Analyze article failed. idArticlesRaw={}", idArticlesRaw, e);
            rawEntity.setStatus(ArticlesRawStatus.ERROR);
            rawEntity.setErrorMessage(e.getMessage());
        } finally {
            articleRawRepository.save(rawEntity);
        }
    }

    /**
     * Kiểm tra toàn diện bài báo dựa trên cả Title và Content.
     *
     * @return true nếu bài báo thuộc dạng KHÔNG liên quan (RÁC).
     */
    public static boolean isIrrelevantArticle(String title, String content) {

        if (title == null || title.isEmpty()) {
            return true;
        }

        String[] blacklistKeywords = ConfigCache
                .getConfig(Constants.ARTICLE_BLACKLIST_KEYWORDS)
                .toLowerCase()
                .split(",");

        String lowerTitle = title.toLowerCase();
        String lowerContent = content == null ? "" : content.toLowerCase();

        return Arrays.stream(blacklistKeywords)
                .map(String::trim)
                .anyMatch(keyword ->
                        lowerTitle.contains(keyword)
                                || (!lowerContent.isEmpty()
                                && lowerContent.contains(keyword)));
    }

    /**
     * TRÍCH XUẤT TẤT CẢ CÁC MÃ CỔ PHIẾU XUẤT HIỆN TRONG TIÊU ĐỀ
     *
     * @return List<String> chứa danh sách các mã (Ví dụ: ["HPG", "SSI", "VHM"]) hoặc mảng rỗng nếu là tin vĩ mô.
     */
    public List<String> extractTickers(String title) {

        if (title == null || title.isBlank()) {
            return Collections.emptyList();
        }
        Set<String> detectedTickers = new HashSet<>();
        Set<String> tickerConfig = getTickerSet();
        Matcher matcher = TICKER_PATTERN.matcher(title.toUpperCase());
        while (matcher.find()) {
            String word = matcher.group();
            if (tickerConfig.contains(word)) {
                detectedTickers.add(word);
            }
        }

        return new ArrayList<>(detectedTickers);
    }

    private static Set<String> getTickerSet() {
        String tickerConfig = ConfigCache.getConfig(Constants.TICKER_LIST);
        if (!tickerConfig.equals(lastTickerConfig)) {
            tickerSet = Arrays.stream(tickerConfig.split(";"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toSet());

            lastTickerConfig = tickerConfig;
        }
        return tickerSet;
    }
}
