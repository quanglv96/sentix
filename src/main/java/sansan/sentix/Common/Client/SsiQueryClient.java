package sansan.sentix.Common.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sansan.sentix.Common.Config.FeignConfig;
import sansan.sentix.Module.Articles.Request.SSI.SsiStockMultipleReq;
import sansan.sentix.Module.Articles.Response.SSI.SsiResponse;
import sansan.sentix.Module.Articles.Response.SSI.SsiStockInfoRes;
import sansan.sentix.Module.Articles.Response.SSI.SsiStockMultipleRes;

import java.util.List;

@FeignClient(name = "SsiQueryClient", url = "${ssi.host.query}", configuration = FeignConfig.class)
public interface SsiQueryClient {
    @GetMapping("/stock/stock-info")
    SsiResponse<List<SsiStockInfoRes>> getStockInfo();

    @PostMapping("/stock/multiple")
    SsiResponse<List<SsiStockMultipleRes>> getSessionPrices(@RequestBody SsiStockMultipleReq ssiStockMultipleReq);
}
