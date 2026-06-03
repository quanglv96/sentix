package sansan.sentix.Response.SSI;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SsiStockInfoRes {
    @JsonProperty("code")
    private String code;

    @JsonProperty("clientName")
    private String clientName;

    @JsonProperty("type")
    private String type;
}


