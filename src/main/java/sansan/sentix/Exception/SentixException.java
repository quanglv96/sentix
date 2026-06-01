package sansan.sentix.Exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sansan.sentix.Utils.StringUtils;

@Setter
@Getter
@NoArgsConstructor
public class SentixException extends RuntimeException {
    private String status;

    public SentixException(Object... args) {
        super(StringUtils.appenString(args));
        this.status = "ERROR";
    }
}
