package sansan.sentix.Module.AiIntegration.Factory;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import sansan.sentix.Common.Exception.ErrorCode;
import sansan.sentix.Common.Exception.SentixException;
import sansan.sentix.Module.AiIntegration.Service.AiService;
import sansan.sentix.Module.AiIntegration.Utils.AiEnum;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Log4j2
public class AiFactory {

    private final Map<AiEnum, AiService> serviceMap;

    public AiFactory(List<AiService> strategies) {

        this.serviceMap = strategies.stream()
                .collect(Collectors.toMap(
                        AiService::getStrategy,
                        strategy -> strategy
                ));
    }

    public AiService getService() {
        if (CollectionUtils.isEmpty(serviceMap)) {
            log.error("AiFactory::getService: No crawl services available");
            throw new SentixException(ErrorCode.BUSINESS_ERROR);
        }
        AiService strategy = serviceMap.get(AiEnum.GEMINI);
        if (strategy == null) {
            log.error("AiFactory::getService: No crawl services available {}", AiEnum.GEMINI);
            throw new SentixException(ErrorCode.BUSINESS_ERROR);
        }
        return strategy;
    }
}
