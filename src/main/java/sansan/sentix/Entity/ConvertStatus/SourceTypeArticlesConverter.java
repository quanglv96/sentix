package sansan.sentix.Entity.ConvertStatus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sansan.sentix.Utils.SourceTypeArticles;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
@Component
public class SourceTypeArticlesConverter implements AttributeConverter<SourceTypeArticles, Integer> {
    @Override
    public Integer convertToDatabaseColumn(SourceTypeArticles attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public SourceTypeArticles convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return SourceTypeArticles.fromValue(dbData);
    }
}
