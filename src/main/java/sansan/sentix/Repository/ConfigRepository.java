package sansan.sentix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sansan.sentix.Entity.Config;

import java.util.List;

@Repository
public interface ConfigRepository extends JpaRepository<Config, String> {
    List<Config> findAllByStatus(String value);

    Config findByKeyAndTypeAndStatus(String key, String type, String status);

    Config findByIdAndStatus(String id, String status);

    @Modifying
    @Transactional
    @Query("update Config c set c.value = :value  where c.type = :type and c.key = :key")
    void updateConfigByTypeAndKey(@Param("type") String type, @Param("key") String key, @Param("value") String value);
}
