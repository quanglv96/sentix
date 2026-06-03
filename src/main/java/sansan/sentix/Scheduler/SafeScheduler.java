package sansan.sentix.Scheduler;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;


@Component
@Log4j2
public abstract class SafeScheduler {

    public void runSafe(String job, Runnable action) {
        try {
            log.info("[{}] START", job);
            action.run();
            log.info("[{}] END", job);
        } catch (Exception e) {
            log.error("[{}] JOB FAILED", job, e);
//            telegramService.sendException(e.getMessage(), job);
        }
    }
}
