package sansan.sentix.Exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sansan.sentix.Utils.StringUtils;

@Setter
@Getter
@NoArgsConstructor
public class SentixException extends RuntimeException {
    private ErrorCode errorCode;

    public SentixException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
