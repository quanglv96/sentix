package sansan.sentix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sansan.sentix.Entity.TickerSessionAnalyticsEntity;

@Repository
public interface TickerSessionAnalyticsEntityRepository extends JpaRepository<TickerSessionAnalyticsEntity, Long> {
}
