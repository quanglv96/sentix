package sansan.sentix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sansan.sentix.Entity.CrawlTargetEntity;
import sansan.sentix.Utils.ArticlesRawStatus;

import java.util.List;

@Repository
public interface CrawlTargetRepository extends JpaRepository<CrawlTargetEntity, Long> {
    List<CrawlTargetEntity> findAllByStatus(ArticlesRawStatus status);
}
