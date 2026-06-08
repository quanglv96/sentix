package sansan.sentix.Module.Articles.Entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import sansan.sentix.Module.Articles.Convert.ArticlesRawStatusConverter;
import sansan.sentix.Entity.ConvertStatus.SourceTypeArticlesConverter;
import sansan.sentix.common.Utils.ArticlesRawStatus;
import sansan.sentix.common.Utils.SourceTypeArticles;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "ST_ARTICLES_RAW")
@Data
public class ArticlesRawEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_PUBLISH")
    private String idPublish;

    @Column(name = "TICKER")
    private String ticker;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "SOURCE_URL")
    private String sourceUrl;

    @Lob
    @Column(name = "RAW_CONTENT")
    private String rawContent;

    @Column(name = "PUBLISHED_AT")
    private LocalDateTime publishedAt;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "STATUS")
    @Convert(converter = ArticlesRawStatusConverter.class)
    private ArticlesRawStatus status;

    @Column(name = "SOURCE_TYPE")
    @Convert(converter = SourceTypeArticlesConverter.class)
    private SourceTypeArticles sourceType;

    @Column(name = "ERROR_MESSAGE", length = 2000)
    private String errorMessage;

    @PrePersist
    @PreUpdate
    private void beforeSave() {
        errorMessage = StringUtils.truncate(errorMessage, 2000);
    }
}
