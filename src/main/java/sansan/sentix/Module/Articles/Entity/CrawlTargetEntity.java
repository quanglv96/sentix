package sansan.sentix.Module.Articles.Entity;

import lombok.Data;
import sansan.sentix.Module.Articles.Convert.ArticlesRawStatusConverter;
import sansan.sentix.Module.Articles.Convert.SourceTypeArticlesConverter;
import sansan.sentix.Module.Articles.Utils.ArticlesRawStatus;
import sansan.sentix.Module.Articles.Utils.SourceTypeArticles;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "ST_CRAWL_TARGET")
@Data
public class CrawlTargetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "URL")
    private String url;

    @Column(name = "SOURCE_TYPE")
    @Convert(converter = SourceTypeArticlesConverter.class)
    private SourceTypeArticles sourceType;

    @Column(name = "BUSINESS_TYPE")
    private String businessType;

    @Column(name = "STATUS")
    @Convert(converter = ArticlesRawStatusConverter.class)
    private ArticlesRawStatus status;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
