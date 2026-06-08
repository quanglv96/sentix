package sansan.sentix.Module.Market.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sansan.sentix.Module.Market.Entity.TickerEntity;

@Repository
public interface TickerRepository extends JpaRepository<TickerEntity, Long> {
}
