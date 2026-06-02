package sansan.sentix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sansan.sentix.Entity.SectorEntity;

@Repository
public interface SectorRepository extends JpaRepository<SectorEntity, Long> {
}
