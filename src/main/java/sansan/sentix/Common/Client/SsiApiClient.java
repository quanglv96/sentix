package sansan.sentix.Common.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import sansan.sentix.Common.Config.FeignConfig;
import sansan.sentix.Module.Articles.Response.SSI.SsiResponse;
import sansan.sentix.Module.Articles.Response.SSI.SsiSectorsDataRes;

import java.util.List;

@FeignClient(name = "SsiApiClient", url = "${ssi.host.api}", configuration = FeignConfig.class)
public interface SsiApiClient {
    @GetMapping("/statistics/company/sectors-data-v2")
    SsiResponse<List<SsiSectorsDataRes>> getSectorsData();
}
