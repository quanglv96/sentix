package sansan.sentix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sansan.sentix.Entity.UserPositionEntity;

@Repository
public interface UserPositionRepository extends JpaRepository<UserPositionEntity, Long> {
}
