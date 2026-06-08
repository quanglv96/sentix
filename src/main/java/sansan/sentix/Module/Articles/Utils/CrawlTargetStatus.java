package sansan.sentix.Module.Articles.Utils;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum CrawlTargetStatus {
    ACTIVE(1),
    INACTIVE(2),
    ;

    private final Integer value;

    CrawlTargetStatus(Integer value) {
        this.value = value;
    }

    public static CrawlTargetStatus fromValue(Integer dbData) {
        for (CrawlTargetStatus status : CrawlTargetStatus.values()) {
            if (Objects.equals(status.value, dbData)) {
                return status;
            }
        }
        throw new IllegalArgumentException("[Error][CrawlTargetStatus] Unknown DB value: " + dbData);
    }

}