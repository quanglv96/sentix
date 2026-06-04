package sansan.sentix.Utils;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum ConfigStatus {
    ACTIVE(1),
    INACTIVE(2),
    ;

    private final Integer value;

    ConfigStatus(Integer value) {
        this.value = value;
    }

    public static ConfigStatus fromValue(Integer dbData) {
        for (ConfigStatus status : ConfigStatus.values()) {
            if (Objects.equals(status.value, dbData)) {
                return status;
            }
        }
        throw new IllegalArgumentException("[Error][ConfigStatus] Unknown DB value: " + dbData);
    }

}