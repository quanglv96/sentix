package sansan.sentix.Module.Articles.Request.SSI;

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
public class SsiStockMultipleReq {
    @JsonProperty("stocks")
    private List<String> stocks;
}
