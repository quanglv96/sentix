package sansan.sentix.Module.Market.Converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sansan.sentix.Module.Market.Utils.MarketSession;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
@Component
public class MarketSessionConverter implements AttributeConverter<MarketSession, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MarketSession attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public MarketSession convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return MarketSession.fromValue(dbData);
    }
}
