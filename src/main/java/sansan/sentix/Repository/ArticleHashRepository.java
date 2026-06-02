package sansan.sentix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sansan.sentix.Entity.ArticleHashEntity;

@Repository
public interface ArticleHashRepository extends JpaRepository<ArticleHashEntity, Long> {
}
