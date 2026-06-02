package sansan.sentix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sansan.sentix.Entity.UserConfigEntity;

@Repository
public interface UserConfigRepository extends JpaRepository<UserConfigEntity, Long> {
}
