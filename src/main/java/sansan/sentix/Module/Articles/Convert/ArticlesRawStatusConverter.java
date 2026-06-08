package sansan.sentix.Module.Articles.Convert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sansan.sentix.Module.Articles.Utils.ArticlesRawStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
@Component
public class ArticlesRawStatusConverter implements AttributeConverter<ArticlesRawStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ArticlesRawStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public ArticlesRawStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return ArticlesRawStatus.fromValue(dbData);
    }
}
