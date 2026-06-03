package sansan.sentix.Service;

import sansan.sentix.Utils.MarketSession;

public interface MarketDataSyncService {

    void syncSectors();

    void syncTickers();

    void syncSessionPrices(MarketSession marketSession);
}