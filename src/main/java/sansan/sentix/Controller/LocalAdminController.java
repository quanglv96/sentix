package sansan.sentix.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sansan.sentix.Module.Market.Service.MarketDataSyncService;
import sansan.sentix.Module.Market.Utils.MarketSession;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/local-admin")
public class LocalAdminController {
    @Resource
    private MarketDataSyncService marketDataSyncService;

    @GetMapping("/stock-info")
    public void crawlStockInfo() {
        marketDataSyncService.syncTickers();
    }

    @GetMapping("/sector-data")
    public void crawlSectorData() {
        marketDataSyncService.syncSectors();
    }

    @GetMapping("/session-prices")
    public void syncSessionPrices() {
        marketDataSyncService.syncSessionPrices(MarketSession.MID_DAY);
    }

    @GetMapping("/crawl-last-news")
    public void crawlLatestNews() {
        marketDataSyncService.crawlLatestNews();
    }
}
