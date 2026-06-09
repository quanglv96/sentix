package sansan.sentix.Module.AiIntegration.Converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sansan.sentix.Module.AiIntegration.Utils.GeminiKeyStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
@Component
public class GeminiKeyStatusConverter implements AttributeConverter<GeminiKeyStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(GeminiKeyStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public GeminiKeyStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return GeminiKeyStatus.fromValue(dbData);
    }
}
