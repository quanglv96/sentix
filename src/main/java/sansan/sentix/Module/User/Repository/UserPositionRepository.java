package sansan.sentix.Module.User.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sansan.sentix.Module.User.Entity.UserPositionEntity;

@Repository
public interface UserPositionRepository extends JpaRepository<UserPositionEntity, Long> {
}
