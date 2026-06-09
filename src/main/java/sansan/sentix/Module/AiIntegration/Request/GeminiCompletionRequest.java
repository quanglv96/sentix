package sansan.sentix.Module.AiIntegration.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeminiCompletionRequest {

    @JsonProperty("contents")
    private List<Content> contents;

    @JsonProperty("generationConfig")
    private GenerationConfig generationConfig;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Content {
        @JsonProperty("parts")
        private List<Part> parts;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Part {
        @JsonProperty("text")
        private String text;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GenerationConfig {
        @JsonProperty("responseMimeType")
        private String responseMimeType; // Đặt giá trị là "application/json"
    }
}