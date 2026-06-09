package sansan.sentix.Module.AiIntegration.Utils;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum GeminiKeyStatus {
    ACTIVE(1),
    INACTIVE(2),
    ;

    private final Integer value;

    GeminiKeyStatus(Integer value) {
        this.value = value;
    }

    public static GeminiKeyStatus fromValue(Integer dbData) {
        for (GeminiKeyStatus status : GeminiKeyStatus.values()) {
            if (Objects.equals(status.value, dbData)) {
                return status;
            }
        }
        throw new IllegalArgumentException("[Error][GeminiKeyStatus] Unknown DB value: " + dbData);
    }

}