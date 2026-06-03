package sansan.sentix.Mapping;

import org.springframework.stereotype.Component;
import sansan.sentix.Entity.TickerSessionAnalyticsEntity;
import sansan.sentix.Response.SSI.SsiStockMultipleRes;
import sansan.sentix.Utils.Constants;
import sansan.sentix.Utils.MarketSession;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Component
public class MarketDataMapping {
    // Khai báo hằng số giá trị mặc định cho các trường chưa tính toán ở giai đoạn 1
    private static final BigDecimal DEFAULT_NOT_CALCULATED = new BigDecimal("-1.00");

    public TickerSessionAnalyticsEntity mapSsiResponseToEntity(SsiStockMultipleRes response, MarketSession session) {
        TickerSessionAnalyticsEntity entity = new TickerSessionAnalyticsEntity();

        // 1. MAPPING ĐỊNH DANH CƠ BẢN
        entity.setTicker(response.getStockSymbol());

        if (response.getTradingDate() != null && !response.getTradingDate().isEmpty()) {
            LocalDate localDate = LocalDate.parse(response.getTradingDate(), Constants.DATE_TIME_FORMATTER_6);
            entity.setAnalyticsDate(localDate);
        }
        entity.setMarketSession(session);

        // 2. XỬ LÝ FALLBACK GIÁ CHỐT PHIÊN (Bảo vệ hệ thống khỏi Null)
        if (response.getMatchedPrice() != null && response.getMatchedPrice().compareTo(BigDecimal.ZERO) > 0) {
            entity.setClosePrice(response.getMatchedPrice());
        } else if (response.getRefPrice() != null) {
            // Cổ phiếu mất thanh khoản -> Lấy giá tham chiếu làm giá chốt phiên
            entity.setClosePrice(response.getRefPrice());
        } else {
            // Trường hợp hy hữu nhất -> Lấy giá đóng cửa hôm trước
            entity.setClosePrice(response.getPriorClosePrice());
        }

        // 3. XỬ LÝ FALLBACK KHỐI LƯỢNG
        if (response.getStockVol() != null) {
            entity.setVolume(response.getStockVol());
        } else {
            // Không có giao dịch thì khối lượng lũy kế bằng 0
            entity.setVolume(0L);
        }

        // 4. ĐIỀN ĐẦY ĐỦ CÁC TRƯỜNG GIAI ĐOẠN 2 VỚI VALUE -1
        entity.setTickerSentiment(DEFAULT_NOT_CALCULATED);
        entity.setSectorSentiment(DEFAULT_NOT_CALCULATED);
        entity.setTaRsi14(DEFAULT_NOT_CALCULATED);
        entity.setTaMa20(DEFAULT_NOT_CALCULATED);
        entity.setValPe(DEFAULT_NOT_CALCULATED);
        entity.setValPb(DEFAULT_NOT_CALCULATED);

        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }
}
