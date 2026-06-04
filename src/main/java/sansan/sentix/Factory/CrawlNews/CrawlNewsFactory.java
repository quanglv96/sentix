package sansan.sentix.Factory.CrawlNews;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import sansan.sentix.Exception.ErrorCode;
import sansan.sentix.Exception.SentixException;
import sansan.sentix.Utils.SourceTypeArticles;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class CrawlNewsFactory {
    private static final Logger log = LoggerFactory.getLogger(CrawlNewsFactory.class);
    private final Map<SourceTypeArticles, CrawlNewsService> serviceMap;

    public CrawlNewsFactory(List<CrawlNewsService> strategies) {

        this.serviceMap = strategies.stream()
                .collect(Collectors.toMap(
                        CrawlNewsService::getSourceType,
                        strategy -> strategy
                ));
    }

    public CrawlNewsService getService(SourceTypeArticles sourceType) {
        if (CollectionUtils.isEmpty(serviceMap)) {
            log.error("CrawlNewsFactory::getService: No crawl services available");
            throw new SentixException(ErrorCode.BUSINESS_ERROR);
        }
        CrawlNewsService strategy = serviceMap.get(sourceType);
        if (strategy == null) {
            log.error("CrawlNewsFactory::getService: No crawl services available {}", sourceType);
            throw new SentixException(ErrorCode.BUSINESS_ERROR);
        }
        return strategy;
    }
}
