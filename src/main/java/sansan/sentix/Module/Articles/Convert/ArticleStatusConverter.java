package sansan.sentix.Module.Articles.Convert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sansan.sentix.Module.Articles.Utils.ArticleStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
@Component
public class ArticleStatusConverter implements AttributeConverter<ArticleStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ArticleStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public ArticleStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return ArticleStatus.fromValue(dbData);
    }
}
