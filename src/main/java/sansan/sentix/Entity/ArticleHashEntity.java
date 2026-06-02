package sansan.sentix.Entity;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "ST_ARTICLE_HASHES")
@Data
public class ArticleHashEntity {

    @Id
    @Column(name = "title_hash", length = 64)
    private String titleHash;

    @Column(name = "ARTICLE_ID", length = 64)
    private Long articleId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}