package sansan.sentix.Scheduler;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sansan.sentix.Service.MarketDataSyncService;
import sansan.sentix.Utils.MarketSession;

import javax.annotation.Resource;

@Component
@Log4j2
public class DailyScheduler extends SafeScheduler {
    @Resource
    private MarketDataSyncService marketDataSyncService;

    @Scheduled(cron = "0 31 11 * * *", zone = "Asia/Ho_Chi_Minh")
    public void syncSessionPricesMidDay() {
        runSafe("syncSessionPricesMidDay", () -> marketDataSyncService.syncSessionPrices(MarketSession.MID_DAY));
    }

    @Scheduled(cron = "0 01 15 * * *", zone = "Asia/Ho_Chi_Minh")
    public void syncSessionPricesEndOfDay() {
        runSafe("syncSessionPricesEndOfDay", () -> marketDataSyncService.syncSessionPrices(MarketSession.END_OF_DAY));
    }

    @Scheduled(cron = "2 * 4-22 * * ?", zone = "Asia/Ho_Chi_Minh")
    public void crawlLatestNews() {
        runSafe("crawlLatestNews", () -> marketDataSyncService.crawlLatestNews());
    }

}
