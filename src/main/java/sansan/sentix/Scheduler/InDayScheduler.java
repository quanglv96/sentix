package sansan.sentix.Scheduler;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sansan.sentix.Module.Articles.ArticlesService;
import sansan.sentix.Module.Articles.Entity.ArticlesRawEntity;

import javax.annotation.Resource;
import java.util.List;

@Component
@Log4j2
public class InDayScheduler {
    @Resource
    private ArticlesService articlesService;

    @Scheduled(fixedDelay = 60000) // Cứ cách 1 phút sau khi lượt trước chạy xong thì chạy tiếp
    public void scanAndAnalyze() {
        // Lấy ra danh sách bài báo PENDING giới hạn (Ví dụ: mỗi phút chỉ xử lý tối đa 10 bài)
        List<ArticlesRawEntity> pendingList = articleRawRepository.findTop10ByStatus(Status.PENDING);

        for (ArticlesRawEntity raw : pendingList) {
            // Gọi Gemini phân tích
            boolean success = articlesService.analyzeArticleSentiment(raw);

            // Nghỉ 4 giây giữa các bài để bảo vệ Quota phút (TPM/RPM) của API Key
            if (success) {
                Thread.sleep(4000);
            }
        }
    }
}
