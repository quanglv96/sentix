package sansan.sentix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sansan.sentix.Entity.TickerEntity;

@Repository
public interface TickerRepository extends JpaRepository<TickerEntity, Long> {
}
