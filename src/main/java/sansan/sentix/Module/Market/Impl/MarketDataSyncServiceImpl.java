package sansan.sentix.Module.Market.Impl;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import sansan.sentix.Common.Utils.Constants;
import sansan.sentix.Module.Articles.ArticlesService;
import sansan.sentix.Common.Client.SsiApiClient;
import sansan.sentix.Common.Client.SsiQueryClient;
import sansan.sentix.Module.Articles.Entity.ArticlesRawEntity;
import sansan.sentix.Module.Market.Entity.SectorEntity;
import sansan.sentix.Module.Market.Entity.TickerEntity;
import sansan.sentix.Module.Market.Entity.TickerSessionAnalyticsEntity;
import sansan.sentix.Module.Market.Mapping.MarketDataMapping;
import sansan.sentix.Module.Market.Repository.SectorRepository;
import sansan.sentix.Module.Market.Repository.TickerRepository;
import sansan.sentix.Module.Market.Repository.TickerSessionAnalyticsEntityRepository;
import sansan.sentix.Module.Articles.Request.SSI.SsiStockMultipleReq;
import sansan.sentix.Module.Articles.Response.SSI.SsiResponse;
import sansan.sentix.Module.Articles.Response.SSI.SsiSectorsDataRes;
import sansan.sentix.Module.Articles.Response.SSI.SsiStockInfoRes;
import sansan.sentix.Module.Articles.Response.SSI.SsiStockMultipleRes;
import sansan.sentix.Module.Market.Service.MarketDataSyncService;
import sansan.sentix.Common.Utils.DateTimeUtils;
import sansan.sentix.Module.Market.Utils.MarketSession;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private ArticlesService articlesService;


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
        List<ArticlesRawEntity> raw=  articlesService.crawlLatestNews();
        log.info("Total new article={}", raw.size());
        if (raw.isEmpty()) {
            log.info("No new article");
            return;
        }
        // 4. Bắn toàn bộ đống bài viết mới tinh thu thập được sang Kafka cho AI xử lý
        for (ArticlesRawEntity article : raw) {
            // Key của Kafka dùng TITLE_HASH để đảm bảo các bài viết trùng lặp đi vào đúng phân vùng (Partition)
            kafkaTemplate.send(Constants.TOPIC_ANALYSIS_NEWS, String.valueOf(article.getId()));
            articlesService.analyzeNewsRaw(article.getId());
        }

    }
}