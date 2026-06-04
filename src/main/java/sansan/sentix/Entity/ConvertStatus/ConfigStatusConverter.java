package sansan.sentix.Entity.ConvertStatus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sansan.sentix.Utils.ConfigStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
@Component
public class ConfigStatusConverter implements AttributeConverter<ConfigStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ConfigStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public ConfigStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return ConfigStatus.fromValue(dbData);
    }
}
