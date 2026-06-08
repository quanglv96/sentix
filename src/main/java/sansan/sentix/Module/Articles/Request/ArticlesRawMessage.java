package sansan.sentix.Module.Articles.Request;

import lombok.Data;
import sansan.sentix.Module.Articles.Utils.SourceTypeArticles;

import java.time.LocalDateTime;

@Data
public class ArticlesRawMessage {

    private String idPublish;

    private String ticker;

    private String title;

    private String sourceUrl;

    private String rawContent;

    private LocalDateTime publishedAt;

    private SourceTypeArticles sourceType;
}
