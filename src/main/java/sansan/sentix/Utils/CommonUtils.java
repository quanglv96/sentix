package sansan.sentix.Utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import sansan.sentix.Exception.ErrorCode;
import sansan.sentix.Exception.SentixException;

import java.util.Map;

@Slf4j
public class CommonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static <T> T convertJsonToObject(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T convertJsonToListObject(String jsonString, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(jsonString, typeReference);
        } catch (Exception e) { // Hoặc IOException tùy version Jackson
            throw new RuntimeException("Error converting JSON string to object (TypeReference): " + jsonString, e);
        }
    }

    public static String convertObjectToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Error converting JSON string to object: {}", obj, e);
            throw new SentixException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
    }

    public static Map<String, Object> readValue(byte[] buf) {
        try {
            return objectMapper.readValue(buf, new TypeReference<>() {});
        } catch (Exception e) { // Hoặc IOException tùy version Jackson
            log.error("Error converting byte array to Map (TypeReference): {}", String.valueOf(e));
            throw new SentixException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
    }

}
