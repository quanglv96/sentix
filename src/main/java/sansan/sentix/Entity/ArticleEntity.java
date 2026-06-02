package sansan.sentix.Entity;

import lombok.Data;
import sansan.sentix.Entity.ConvertStatus.ArticleStatusConverter;
import sansan.sentix.Utils.ArticleStatus;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ST_ARTICLES")
@Data
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "st_articles_seq")
    @SequenceGenerator(name = "st_articles_seq", sequenceName = "seq_st_articles", allocationSize = 1)
    private Long id;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "sector_id")
    private Long sectorId;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "source_url")
    private String sourceUrl;

    @Lob
    @Column(name = "content_summary")
    private String contentSummary; // Chuỗi JSON lưu 3 dòng tóm tắt

    @Column(name = "sentiment_score")
    private BigDecimal sentimentScore;

    @Column(name = "status")
    @Convert(converter = ArticleStatusConverter.class)
    private ArticleStatus status;

    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}