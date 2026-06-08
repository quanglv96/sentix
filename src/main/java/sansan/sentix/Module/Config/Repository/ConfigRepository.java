package sansan.sentix.Module.Config.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sansan.sentix.Module.Config.Entity.Config;
import sansan.sentix.Module.Config.Utils.ConfigStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<Config, String> {
    Optional<Config> findByConfigKey(String key);

    List<Config> findAllByStatus(ConfigStatus configStatus);
}
