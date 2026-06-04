package sansan.sentix.Service.Impl;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import sansan.sentix.Client.SsiApiClient;
import sansan.sentix.Client.SsiQueryClient;
import sansan.sentix.Config.AsyncConfig;
import sansan.sentix.Entity.ArticleHashEntity;
import sansan.sentix.Entity.ArticlesRawEntity;
import sansan.sentix.Entity.CrawlTargetEntity;
import sansan.sentix.Entity.SectorEntity;
import sansan.sentix.Entity.TickerEntity;
import sansan.sentix.Entity.TickerSessionAnalyticsEntity;
import sansan.sentix.Factory.CrawlNews.CrawlNewsFactory;
import sansan.sentix.Mapping.MarketDataMapping;
import sansan.sentix.Repository.ArticleHashRepository;
import sansan.sentix.Repository.ArticleRawRepository;
import sansan.sentix.Repository.CrawlTargetRepository;
import sansan.sentix.Repository.SectorRepository;
import sansan.sentix.Repository.TickerRepository;
import sansan.sentix.Repository.TickerSessionAnalyticsEntityRepository;
import sansan.sentix.Request.ArticlesRawMessage;
import sansan.sentix.Request.SSI.SsiStockMultipleReq;
import sansan.sentix.Response.SSI.SsiResponse;
import sansan.sentix.Response.SSI.SsiSectorsDataRes;
import sansan.sentix.Response.SSI.SsiStockInfoRes;
import sansan.sentix.Response.SSI.SsiStockMultipleRes;
import sansan.sentix.Service.ArticlesRawService;
import sansan.sentix.Service.MarketDataSyncService;
import sansan.sentix.Utils.ArticlesRawStatus;
import sansan.sentix.Utils.Constants;
import sansan.sentix.Utils.DateTimeUtils;
import sansan.sentix.Utils.MarketSession;
import sansan.sentix.Utils.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Log4j2
public class MarketDataSyncServiceImpl implements MarketDataSyncService {
    @Resource
    private SsiApiClient ssiApiClient;
    @Resource
    private SsiQueryClient ssiQueryClient;
    @Resource
    private TickerRepository tickerRepository;
    @Resource
    private SectorRepository sectorRepository;
    @Resource
    private MarketDataMapping marketDataMapping;
    @Resource
    private TickerSessionAnalyticsEntityRepository tickerSessionAnalyticsEntityRepository;
    @Resource
    private CrawlTargetRepository crawlTargetRepository;
    @Resource
    private CrawlNewsFactory crawlNewsFactory;
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private ArticleRawRepository articleRawRepository;
    @Resource
    private ArticleHashRepository articleHashRepository;
    @Resource
    private ArticlesRawService articlesRawService;
    @Resource
    @Qualifier("crawlNews")
    private AsyncConfig asyncConfig;

    @Override
    public void syncSectors() { // phân loại ngành cấp 2
        SsiResponse<List<SsiSectorsDataRes>> sectorsData = ssiApiClient.getSectorsData();
        List<SsiSectorsDataRes> dataList = sectorsData.getData();
        List<SectorEntity> sectorEntityList = new ArrayList<>();
        for (SsiSectorsDataRes data : dataList) {
            if (data.getIndustryLevel() != 2) {
                continue;
            }
            SectorEntity sectorEntity = new SectorEntity();
            sectorEntity.setName(data.getIndustryName().getVi());
            sectorEntity.setIndustryCode(data.buildCompany());
            sectorEntityList.add(sectorEntity);
        }
        sectorEntityList = sectorRepository.saveAll(sectorEntityList);
        // phân loại
        List<TickerEntity> tickerEntityList = tickerRepository.findAll();
        Map<String, TickerEntity> tickerMap = tickerEntityList.stream().collect(Collectors.toMap(TickerEntity::getTicker, ticker -> ticker));
        for (SectorEntity sector : sectorEntityList) {
            if (sector.getIndustryCode() == null) {
                continue;
            }
            String[] tickers = sector.getIndustryCode().split(";");
            for (String tickerCode : tickers) {
                TickerEntity ticker = tickerMap.get(tickerCode.trim());
                if (ticker != null) {
                    ticker.setSectorId(sector.getId());
                }
            }
        }
        tickerRepository.saveAll(tickerMap.values());
    }

    @Override
    public void syncTickers() { // mã cổ phiếu
        SsiResponse<List<SsiStockInfoRes>> stockInfo = ssiQueryClient.getStockInfo();
        List<SsiStockInfoRes> dataList = stockInfo.getData().stream().filter(m -> "s".equals(m.getType())).collect(Collectors.toList());

        List<TickerEntity> tickerEntities = tickerRepository.findAll();
        Map<String, TickerEntity> tickers = tickerEntities.stream().collect(Collectors.toMap(TickerEntity::getTicker, ticker -> ticker));
        for (SsiStockInfoRes data : dataList) {
            if (ObjectUtils.isNotEmpty(tickers.get(data.getCode()))) {
                continue;
            }
            TickerEntity ticker = new TickerEntity();
            ticker.setTicker(data.getCode());
            ticker.setCompanyName(data.getClientName());
            ticker.setUpdatedAt(DateTimeUtils.nowLocalDateTime());
            tickers.put(ticker.getTicker(), ticker);
        }
        tickerRepository.saveAll(tickers.values());
    }

    @Override
    public void syncSessionPrices(MarketSession marketSession) { // đồng bộ giá theo phiên
        // danh sách ngành
        List<SectorEntity> sectorEntities = sectorRepository.findAll();
        List<SsiStockMultipleRes> sessionPrices = new ArrayList<>();
        for (SectorEntity sector : sectorEntities) {
            List<String> body = List.of(sector.getIndustryCode().split(";"));
            SsiResponse<List<SsiStockMultipleRes>> multiDataStock = ssiQueryClient.getSessionPrices(SsiStockMultipleReq.builder().stocks(body).build());
            sessionPrices.addAll(multiDataStock.getData());
        }
        List<TickerSessionAnalyticsEntity> entitiesToSave = new ArrayList<>();
        for (SsiStockMultipleRes data : sessionPrices) {
            TickerSessionAnalyticsEntity entity = marketDataMapping.mapSsiResponseToEntity(data, marketSession);
            entitiesToSave.add(entity);
        }

        // GHI XUỐNG DATABASE THEO LÔ (Chỉ tốn vài câu lệnh thay vì 2000 lệnh)
        if (!entitiesToSave.isEmpty()) {
            tickerSessionAnalyticsEntityRepository.saveAll(entitiesToSave);
        }
        //TODO: luu redis chạy batch dần
    }

    @Override
    public void crawlLatestNews() {
        List<CrawlTargetEntity> targets = crawlTargetRepository.findAllByStatus(ArticlesRawStatus.ACTIVE);
        if (targets.isEmpty()) {
            //TODO: thông báo admin
            return;
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
        log.info("Total new article={}", raw.size());
        if (raw.isEmpty()) {
            log.info("No new article");
            return;
        }
        // luwu hash

        // 4. Bắn toàn bộ đống bài viết mới tinh thu thập được sang Kafka cho AI xử lý
        for (ArticlesRawEntity article : raw) {
            // Key của Kafka dùng TITLE_HASH để đảm bảo các bài viết trùng lặp đi vào đúng phân vùng (Partition)
            kafkaTemplate.send(Constants.TOPIC_ANALYSIS_NEWS, String.valueOf(article.getId()));
//            articlesRawService.analyzeNewsRaw(article.getId());
        }

    }
}