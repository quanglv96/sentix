package sansan.sentix.Module.Articles.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sansan.sentix.Module.Articles.Entity.CrawlTargetEntity;
import sansan.sentix.Module.Articles.Utils.ArticlesRawStatus;

import java.util.List;

@Repository
public interface CrawlTargetRepository extends JpaRepository<CrawlTargetEntity, Long> {
    List<CrawlTargetEntity> findAllByStatus(ArticlesRawStatus status);
}
