package sansan.sentix.Module.AiIntegration.Factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sansan.sentix.Module.AiIntegration.Entity.GeminiKeyEntity;
import sansan.sentix.Module.AiIntegration.Repository.GeminiKeyRepository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class GeminiFactory {

    @Resource
    private GeminiKeyRepository geminiKeyRepository;

    /**
     * HÀM 1: GỌI KEY ĐƯỢC CHỌN (CHỈ LẤY KEY KHẢ DỤNG)
     * Đồng thời tăng bộ đếm daily_count lên 1 dưới DB để xí phần token.
     */
    @Transactional
    public synchronized GeminiKeyEntity getAvailableKey() {
        // Gọi câu Query từ Repository để lọc các key sạch
        List<GeminiKeyEntity> availableKeys = geminiKeyRepository.findAvailableKeys();

        if (availableKeys == null || availableKeys.isEmpty()) {
            log.error("🚨 [GEMINI FACTORY] Toàn bộ API Keys trong DB đều đã hết hạn mức hoặc đang bị khóa phạt!");
            return null;
        }

        // Chọn con đầu tiên trong danh sách (Tự động xoay vòng dựa trên thứ tự db trả về)
        GeminiKeyEntity selectedKey = availableKeys.get(0);

        // Tăng bộ đếm Request trong ngày của Key này lên 1
        selectedKey.setDailyCount(selectedKey.getDailyCount() + 1);
        geminiKeyRepository.save(selectedKey);

        log.info("🔀 [GEMINI FACTORY] Nhặt thành công Key: [{}] (Hôm nay đã gọi: {} lần)",
                selectedKey.getAccountName(), selectedKey.getDailyCount());

        return selectedKey;
    }

    /**
     * HÀM 2: CẬP NHẬT TRẠNG THÁI KHI DÍNH LỖI 429 (RATE LIMIT)
     * Tự động phân tích xem do hết hạn mức Phút hay cạn kiệt hạn mức Ngày.
     * * @param keyEntity Đối tượng Key hiện tại vừa gọi bị lỗi
     * @param errorBody Nội dung chuỗi JSON lỗi trả về từ Google để bắt từ khóa
     */
    @Transactional
    public void handleRateLimitError(GeminiKeyEntity keyEntity, String errorBody) {
        if (keyEntity == null) return;

        // Ép tìm lại bản ghi chính xác từ DB để tránh dữ liệu cũ (Detached Entity)
        GeminiKeyEntity dbKey = geminiKeyRepository.findById(keyEntity.getId()).orElse(null);
        if (dbKey == null) return;

        if (errorBody != null && (errorBody.contains("DAILY_QUOTA_EXCEEDED") || errorBody.contains("Quota exceeded"))) {
            // Trường hợp 1: Hết sạch hạn mức Ngày (1,500 req) -> Đánh dấu đạt đỉnh để loại khỏi luồng quét
            log.warn("🚨 [GEMINI FACTORY] Key [{}] chính thức HẾT HẠN MỨC NGÀY. Khóa đến đêm.", dbKey.getAccountName());
            dbKey.setDailyCount(1400); // Ngưỡng an toàn chặn không cho lấy lên nữa
        } else {
            // Trường hợp 2: Chỉ bị nghẽn Phút (Quá 15 RPM) -> Phạt cho nghỉ 5 phút
            log.warn("⚠️ [GEMINI FACTORY] Key [{}] dính Spam Phút (429). Phạt đóng băng 5 phút.", dbKey.getAccountName());
            dbKey.setBlockedUntil(LocalDateTime.now().plusMinutes(5));
        }

        geminiKeyRepository.save(dbKey);
    }

    /**
     * HÀM 3: JOB RESET LIMIT TỰ ĐỘNG VÀO LÚC 00:00 MỖI ĐÊM
     * Đưa toàn bộ bộ đếm về 0 và gỡ bỏ toàn bộ thời gian phạt khóa để sẵn sàng ngày mới.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void resetLimitDailyJob() {
        log.info("⏰ [CRON JOB] Đến nửa đêm. Tiến hành reset giới hạn cho toàn bộ tài khoản Gemini...");
        try {
            List<GeminiKeyEntity> allKeys = geminiKeyRepository.findAll();
            for (GeminiKeyEntity key : allKeys) {
                key.setDailyCount(0);
                key.setBlockedUntil(null);
            }
            geminiKeyRepository.saveAll(allKeys);
            log.info("✅ [CRON JOB] Reset bộ đếm ngày mới thành công vẹn toàn!");
        } catch (Exception e) {
            log.error("❌ [CRON JOB] Gặp rủi ro khi reset hạn mức Key: {}", e.getMessage());
        }
    }
}