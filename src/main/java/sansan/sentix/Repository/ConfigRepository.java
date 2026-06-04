package sansan.sentix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sansan.sentix.Entity.Config;
import sansan.sentix.Utils.ConfigStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<Config, String> {
    Optional<Config> findByConfigKey(String key);

    List<Config> findAllByStatus(ConfigStatus configStatus);
}
