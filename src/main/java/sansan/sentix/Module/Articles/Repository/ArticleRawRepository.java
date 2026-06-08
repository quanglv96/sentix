package sansan.sentix.Module.Articles.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sansan.sentix.Module.Articles.Entity.ArticlesRawEntity;
import sansan.sentix.common.Utils.ArticlesRawStatus;

@Repository
public interface ArticleRawRepository extends JpaRepository<ArticlesRawEntity, Long> {
    @Query(value = "SELECT CASE\n" +
            "         WHEN EXISTS (\n" +
            "              SELECT 1\n" +
            "              FROM ST_ARTICLE_HASHES\n" +
            "              WHERE TITLE_HASH = :titleHash\n" +
            "         )\n" +
            "         OR EXISTS (\n" +
            "              SELECT 1\n" +
            "              FROM ST_ARTICLES_RAW\n" +
            "              WHERE ID_PUBLISH = :idPublish\n" +
            "                AND SOURCE_TYPE = :sourceType\n" +
            "         )\n" +
            "         THEN 1\n" +
            "         ELSE 0\n" +
            "       END\n" +
            "FROM DUAL", nativeQuery = true)
    long existsByTitleHashAndIdPublishAndSourceType(
            @Param("titleHash") String titleHash,
            @Param("idPublish") String idPublish,
            @Param("sourceType") Integer sourceType
    );

    ArticlesRawEntity findByIdAndStatus(Long id, ArticlesRawStatus status);
}
