package sansan.sentix.Utils;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum ArticlesRawStatus {

    PENDING(0),
    ACTIVE(1),
    READ(2),
    INACTIVE(3),
    ERROR(4);

    private final Integer value;

    ArticlesRawStatus(Integer value) {
        this.value = value;
    }

    public static ArticlesRawStatus fromValue(Integer dbData) {
        for (ArticlesRawStatus status : ArticlesRawStatus.values()) {
            if (Objects.equals(status.value, dbData)) {
                return status;
            }
        }
        throw new IllegalArgumentException("[Error][ArticlesRawStatus] Unknown DB value: " + dbData);
    }

}