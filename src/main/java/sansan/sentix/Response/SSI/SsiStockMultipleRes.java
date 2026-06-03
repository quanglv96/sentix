package sansan.sentix.Response.SSI;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SsiStockMultipleRes {

    @JsonProperty("stockSymbol")
    private String stockSymbol;       // Mã cổ phiếu (Ví dụ: "PVC")

    @JsonProperty("tradingDate")
    private String tradingDate;       // Ngày giao dịch dạng chuỗi (Ví dụ: "20260603")

    @JsonProperty("matchedPrice")
    private BigDecimal matchedPrice;  // Giá khớp gần nhất - chính là giá chốt phiên

    @JsonProperty("stockVol")
    private Long stockVol;            // Tổng khối lượng giao dịch tính đến mốc chốt phiên

    // --- BỔ SUNG CÁC TRƯỜNG DỰ PHÒNG ---
    @JsonProperty("refPrice")
    private BigDecimal refPrice;      // Giá tham chiếu đầu ngày (Luôn luôn có)

    @JsonProperty("priorClosePrice")
    private BigDecimal priorClosePrice; // Giá chốt phiên hôm trước (Luôn luôn có)
}