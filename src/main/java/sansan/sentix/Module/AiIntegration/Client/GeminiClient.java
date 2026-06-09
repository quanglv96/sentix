package sansan.sentix.Module.AiIntegration.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sansan.sentix.Common.Config.FeignConfig;
import sansan.sentix.Module.AiIntegration.Request.GeminiCompletionRequest;
import sansan.sentix.Module.AiIntegration.Response.Request.GeminiCompletionResponse;

@FeignClient(name = "GeminiClient", url = "${ai.gemini}", configuration = FeignConfig.class)
public interface GeminiClient {

    @PostMapping(value = "/v1beta/models/gemini-2.5-flash:generateContent")
    GeminiCompletionResponse callCompletions(@RequestBody GeminiCompletionRequest body, @RequestParam("key") String apiKey);
}
