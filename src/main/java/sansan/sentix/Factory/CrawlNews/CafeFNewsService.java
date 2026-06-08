package sansan.sentix.Factory.CrawlNews;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sansan.sentix.common.Client.CafeFClient;
import sansan.sentix.Request.ArticlesRawMessage;
import sansan.sentix.common.Utils.SourceTypeArticles;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CafeFNewsService implements CrawlNewsService {
    @Resource
    private CafeFClient cafeFClient;
    @Value("${cafe-f.host}")
    private String host;

    @Override
    public SourceTypeArticles getSourceType() {
        return SourceTypeArticles.CAFE_F;
    }

    @Override
    public List<ArticlesRawMessage> crawlLatestNews() {

        String newsHtml = cafeFClient.fetchNews();

        Document doc = Jsoup.parseBodyFragment(newsHtml);
        Elements items = doc.select("li");

        List<ArticlesRawMessage> articles = new ArrayList<>();

        for (Element item : items) {
            Element aTag = item.selectFirst("a");
            Element timeTag = item.selectFirst("span.time");

            if (aTag == null || timeTag == null) {
                continue;
            }
            String id = item.attr("data-id");
            String title = aTag.attr("title");
            String href = aTag.attr("href");
            String time = timeTag.text();                // 09:45
            String month = timeTag.attr("data-month");  // 06
            String day = timeTag.attr("data-date");     // 04
            LocalDate today = LocalDate.now();
            LocalDateTime publishedAt = LocalDateTime.of(
                    today.getYear(),
                    Integer.parseInt(month),
                    Integer.parseInt(day),
                    Integer.parseInt(time.split(":")[0]),
                    Integer.parseInt(time.split(":")[1])
            );

            ArticlesRawMessage article = new ArticlesRawMessage();
            article.setIdPublish(id);
            article.setTitle(title);
            article.setSourceType(getSourceType());
            article.setSourceUrl(host + href);
            article.setPublishedAt(publishedAt);
            articles.add(article);
        }
        return articles;
    }

    @Override
    public String getRawContent(String urlSource) {
        try {
            Document doc = Jsoup.connect(urlSource)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Element content = doc.selectFirst(".detail-content");

            if (content == null) {
                return null;
            }

            return content.select("p")
                    .stream()
                    .map(Element::text)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("\n"));

        } catch (Exception ex) {
            log.error("Get raw content failed. Url: {}", urlSource, ex);
            return null;
        }
    }
}
