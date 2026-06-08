package sansan.sentix.Module.Articles.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sansan.sentix.Module.Articles.Entity.ArticleHashEntity;

@Repository
public interface ArticleHashRepository extends JpaRepository<ArticleHashEntity, Long> {
}
