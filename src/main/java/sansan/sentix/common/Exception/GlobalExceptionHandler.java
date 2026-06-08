package sansan.sentix.common.Exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sansan.sentix.Response.ApiResponse;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                        ErrorCode.UNKNOWN_ERROR.getCode(),
                        ErrorCode.UNKNOWN_ERROR.getMessage(),
                        null
                ));
    }

    @ExceptionHandler(SentixException.class)
    public ResponseEntity<ApiResponse<Object>> handleSentixException(SentixException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(
                        ex.getErrorCode().getCode(),
                        ex.getErrorCode().getMessage(),
                        null
                ));
    }
}
