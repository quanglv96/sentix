package sansan.sentix.Module.User.Utils;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum UserStatus {

    ACTIVE(1),
    ONLINE(2),
    BLOCK(0),
    ;

    private final Integer value;

    UserStatus(Integer value) {
        this.value = value;
    }

    public static UserStatus fromValue(Integer dbData) {
        for (UserStatus status : UserStatus.values()) {
            if (Objects.equals(status.value, dbData)) {
                return status;
            }
        }
        throw new IllegalArgumentException("[Error][UserStatus] Unknown DB value: " + dbData);
    }

}