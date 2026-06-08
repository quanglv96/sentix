package sansan.sentix.Module.Market.Service;

import sansan.sentix.common.Utils.MarketSession;

public interface MarketDataSyncService {

    void syncSectors();

    void syncTickers();

    void syncSessionPrices(MarketSession marketSession);

    void crawlLatestNews();
}