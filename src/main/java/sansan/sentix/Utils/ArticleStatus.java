package sansan.sentix.Utils;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum ArticleStatus {

    ACTIVE(1),
    ERROR(2),
    INACTIVE(3),
    PENDING(4),
    PENDING_APPROVAL(5),
    MANUAL_APPROVAL(6);

    private final Integer value;

    ArticleStatus(Integer value) {
        this.value = value;
    }

    public static ArticleStatus fromValue(Integer dbData) {
        for (ArticleStatus status : ArticleStatus.values()) {
            if (Objects.equals(status.value, dbData)) {
                return status;
            }
        }
        throw new IllegalArgumentException("[Error][CampaignStatus] Unknown DB value: " + dbData);
    }

}