package sansan.sentix.Module.Articles.Utils;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum SourceTypeArticles {

    CAFE_F(1),
    ;
    private final Integer value;

    SourceTypeArticles(Integer value) {
        this.value = value;
    }

    public static SourceTypeArticles fromValue(Integer dbData) {
        for (SourceTypeArticles status : SourceTypeArticles.values()) {
            if (Objects.equals(status.value, dbData)) {
                return status;
            }
        }
        throw new IllegalArgumentException("[Error][SourceTypeArticles] Unknown DB value: " + dbData);
    }

}