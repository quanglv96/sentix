package sansan.sentix.Common.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import sansan.sentix.Common.Config.FeignConfig;

@FeignClient(name = "CafeFClient", url = "${cafe-f.host}", configuration = FeignConfig.class)
public interface CafeFClient {
    @GetMapping(value = "/ajax/tinmoi.chn", produces = "text/html")
    String fetchNews();
}
