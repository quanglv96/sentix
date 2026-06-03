package sansan.sentix.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sansan.sentix.Config.FeignConfig;
import sansan.sentix.Request.SSI.SsiStockMultipleReq;
import sansan.sentix.Response.SSI.SsiResponse;
import sansan.sentix.Response.SSI.SsiStockInfoRes;
import sansan.sentix.Response.SSI.SsiStockMultipleRes;

import java.util.List;

@FeignClient(name = "SsiQueryClient", url = "${ssi.host.query}", configuration = FeignConfig.class)
public interface SsiQueryClient {
    @GetMapping("/stock/stock-info")
    SsiResponse<List<SsiStockInfoRes>> getStockInfo();

    @PostMapping("/stock/multiple")
    SsiResponse<List<SsiStockMultipleRes>> getSessionPrices(@RequestBody SsiStockMultipleReq ssiStockMultipleReq);
}
