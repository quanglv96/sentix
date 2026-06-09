package sansan.sentix.Module.AiIntegration.Strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import sansan.sentix.Common.Request.ArticleSentimentRequest;
import sansan.sentix.Common.Response.ArticleSentimentResponse;
import sansan.sentix.Module.AiIntegration.Client.GeminiClient;
import sansan.sentix.Module.AiIntegration.Entity.GeminiKeyEntity;
import sansan.sentix.Module.AiIntegration.Factory.GeminiFactory;
import sansan.sentix.Module.AiIntegration.Request.GeminiCompletionRequest;
import sansan.sentix.Module.AiIntegration.Response.Request.GeminiCompletionResponse;
import sansan.sentix.Module.AiIntegration.Service.AiService;
import sansan.sentix.Module.AiIntegration.Utils.AiEnum;

import javax.annotation.Resource;
import java.util.Collections;

@Service
@Log4j2
public class GeminiStrategy implements AiService {
    @Resource
    private GeminiFactory geminiFactory;
    @Resource
    private GeminiClient geminiClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AiEnum getStrategy() {
        return AiEnum.GEMINI;
    }

    @Override
    public ArticleSentimentResponse analyzeArticleSentiment(ArticleSentimentRequest request) {
        GeminiKeyEntity geminiKeyEntity = geminiFactory.getAvailableKey();
        String prompt = buildFinancialPrompt(request.getTitle(), request.getContent());

        try {
            // 1. Build POJO Request một cách chuẩn chỉ và an toàn tuyệt đối
            GeminiCompletionRequest.Part part = GeminiCompletionRequest.Part.builder()
                    .text(prompt)
                    .build();

            GeminiCompletionRequest.Content content = GeminiCompletionRequest.Content.builder()
                    .parts(Collections.singletonList(part))
                    .build();

            GeminiCompletionRequest.GenerationConfig config = GeminiCompletionRequest.GenerationConfig.builder()
                    .responseMimeType("application/json")
                    .build();

            GeminiCompletionRequest apiPayload = GeminiCompletionRequest.builder()
                    .contents(Collections.singletonList(content))
                    .generationConfig(config)
                    .build();

            // 2. Thực hiện bắn Cloud API và map thẳng vào Object trung gian GeminiRawResponse
            GeminiCompletionResponse response = geminiClient.callCompletions(apiPayload, geminiKeyEntity.getApiKey());

            // 3. Khai thác dữ liệu an toàn từ Object phản hồi trung gian
            if (response == null || response.getCandidates() == null || response.getCandidates().isEmpty()) {
                throw new RuntimeException("Mô hình không trả về bất kỳ kết quả (candidates) nào.");
            }

            String aiCleanJson = response.getCandidates().get(0)
                    .getContent().getParts().get(0)
                    .getText();

            log.info("🎯 Gemini phản hồi JSON sạch thành công qua FeignClient cho bài: {}", request.getTitle());

            // 4. Đổ chuỗi JSON tinh khiết thu được vào DTO chính thức của hệ thống SENTIX
            return objectMapper.readValue(aiCleanJson, ArticleSentimentResponse.class);
        } catch (feign.FeignException e) {
            // 5. Bắt lỗi HTTP của FeignClient để xử lý phạt Key
            if (e.status() == 429) {
                log.warn("⚠️ Phát hiện Key bị dính Rate Limit (429) qua Feign: {}", geminiKeyEntity.getApiKey());
                // Trích xuất error body từ Feign Exception để kiểm tra hết ngày hay hết phút
                String errorBody = e.contentUTF8();
                geminiFactory.handleRateLimitError(geminiKeyEntity, errorBody);
            }
            throw new RuntimeException("Lỗi gọi FeignClient Gemini (HTTP " + e.status() + ")", e);
        } catch (Exception e) {
            log.error("❌ Lỗi hệ thống khi phân tích bài báo: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String buildFinancialPrompt(String title, String content) {
        return String.format("Bạn là một chuyên gia phân tích dữ liệu tài chính cho thị trường chứng khoán Việt Nam. Hãy đọc kỹ Tiêu đề và Nội dung bài báo sau đây: " +
                "- Tiêu đề: %s " +
                "- Nội dung: %s " +
                "YÊU CẦU: " +
                "1. Xác định bài báo có chứa thông tin tài chính/chứng khoán tác động đến thị trường Việt Nam hay không. " +
                "2. Xác định đây là tin tức riêng của một vài doanh nghiệp (is_industry_news = false) hay là tin tức vĩ mô/tin chung của cả một ngành kinh tế (is_industry_news = true). " +
                "3. Nếu là tin ngành (is_industry_news = true), bạn BẮT BUỘC phải chọn và điền chính xác 1 trong các ngành sau vào trường 'impacted_industry': [" +
                "Hàng & Dịch vụ Công nghiệp, Ô tô và phụ tùng, Thực phẩm và đồ uống, Hàng cá nhân & Gia dụng, Y tế, Bán lẻ, Truyền thông, " +
                "Du lịch và Giải trí, Viễn thông, Điện, nước & xăng dầu khí đốt, Ngân hàng, Bảo hiểm, Bất động sản, Dịch vụ tài chính, " +
                "Công nghệ Thông tin, Dầu khí, Hóa chất, Tài nguyên Cơ bản, Xây dựng và Vật liệu]. Nếu là tin doanh nghiệp cụ thể, để trống trường này và bóc tách mã cổ phiếu vào mảng 'tickers'. " +
                "4. ĐẾM CHÍNH XÁC số lượng luận điểm hoặc sự kiện thực tế tích cực (good_points) và tiêu cực (bad_points) tác động trực tiếp đến doanh nghiệp hoặc ngành đó. " +
                "5. Viết một câu tóm tắt lý do ngắn gọn bằng tiếng Việt (reason). " +
                "QUY TẮC TRẢ VỀ: " +
                "Trả về DUY NHẤT một chuỗi định dạng JSON thuần túy, không kèm theo bất kỳ lời giải thích nào, không bọc trong ký tự ```json. " +
                "Cấu trúc JSON bắt buộc phải như sau: " +
                "{ \"is_financial\": true, " +
                "\"is_industry_news\": true, " +
                "\"impacted_industry\": \"Tài nguyên Cơ bản\", " +
                "\"tickers\": [], " +
                "\"analysis_results\": [ " +
                "{ \"ticker\": \"\", \"good_points\": 2, \"bad_points\": 0, \"reason\": \"Giá thép xuất khẩu tăng mạnh hỗ trợ các doanh nghiệp trong ngành\" } " +
                "] " +
                "}", title, content);
    }
}
