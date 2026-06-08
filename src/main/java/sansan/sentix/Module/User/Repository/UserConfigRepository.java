package sansan.sentix.Module.User.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sansan.sentix.Module.User.Entity.UserConfigEntity;

@Repository
public interface UserConfigRepository extends JpaRepository<UserConfigEntity, Long> {
}
