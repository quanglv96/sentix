package sansan.sentix.Module.AiIntegration.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sansan.sentix.Module.AiIntegration.Entity.GeminiKeyEntity;

import java.util.List;

@Repository
public interface GeminiKeyRepository extends JpaRepository<GeminiKeyEntity, Long> {

    // Chỉ lấy các Key đang Active VÀ (chưa từng bị phạt HOẶC đã hết thời gian phạt phút)
    @Query("SELECT k FROM GeminiKeyEntity k WHERE k.status = 1 AND (k.blockedUntil IS NULL OR k.blockedUntil < CURRENT_TIMESTAMP) AND k.dailyCount < 1400")
    List<GeminiKeyEntity> findAvailableKeys();
}
