package sansan.sentix.Module.Articles.Convert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sansan.sentix.Module.Articles.Utils.CrawlTargetStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
@Component
public class CrawlTargetStatusConverter implements AttributeConverter<CrawlTargetStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(CrawlTargetStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public CrawlTargetStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return CrawlTargetStatus.fromValue(dbData);
    }
}
