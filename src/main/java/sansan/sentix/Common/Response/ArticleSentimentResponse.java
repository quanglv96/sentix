package sansan.sentix.Common.Response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 🔥 Bùa hộ mệnh ở đây: Tự động map snake_case (JSON) thành camelCase (Java)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ArticleSentimentResponse {

    // 1. Kiểm tra xem bài báo có chứa thông tin tài chính/chứng khoán không
    private boolean isFinancial;

    // 🌟 BỔ SUNG: Cờ báo hiệu đây là tin tức vĩ mô/tin chung của cả một ngành kinh tế
    private boolean isIndustryNews;

    // 🌟 BỔ SUNG: Tên ngành bị tác động (Khớp chuẩn 1 trong 19 ngành ICB đã ép trong Prompt)
    private String impactedIndustry;

    // 2. Danh sách tất cả các mã chứng khoán bị tác động (Để trống nếu là tin ngành)
    private List<String> tickers;

    // 3. Danh sách chỉ số phân tích chi tiết
    private List<TickerSentimentDetail> analysisResults;

    /**
     * Class con nội bộ (Inner Class) để bóc tách định lượng
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class) // Đảm bảo class con cũng áp dụng quy tắc này
    public static class TickerSentimentDetail {

        // Mã chứng khoán (Có thể trống nếu là tin ngành)
        private String ticker;

        // Số lượng luận điểm/sự kiện thực tế tích cực tác động
        private int goodPoints;

        // Số lượng luận điểm/sự kiện thực tế tiêu cực tác động
        private int badPoints;

        // Tóm tắt lý do ngắn gọn bằng tiếng Việt từ AI
        private String reason;
    }
}