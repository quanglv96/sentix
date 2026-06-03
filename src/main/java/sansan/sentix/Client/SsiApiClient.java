package sansan.sentix.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import sansan.sentix.Config.FeignConfig;
import sansan.sentix.Response.SSI.SsiResponse;
import sansan.sentix.Response.SSI.SsiSectorsDataRes;

import java.util.List;

@FeignClient(name = "SsiApiClient", url = "${ssi.host.api}", configuration = FeignConfig.class)
public interface SsiApiClient {
    @GetMapping("/statistics/company/sectors-data-v2")
    SsiResponse<List<SsiSectorsDataRes>> getSectorsData();
}
