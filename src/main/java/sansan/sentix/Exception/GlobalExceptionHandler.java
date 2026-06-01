package sansan.sentix.Exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sansan.sentix.DTO.ExceptionDTO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.concurrent.CompletionException;

@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleException(HttpServletRequest request, Exception e) {
        LOGGER.error(e.getMessage());
        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setMessage(e.getMessage());
        exceptionDTO.setErrorCode("ERROR");

        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SentixException.class)
    public ResponseEntity<ExceptionDTO> handleSanException(HttpServletRequest request, SentixException e) {
        LOGGER.error(e.getMessage());
        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setMessage(e.getMessage());
        exceptionDTO.setErrorCode("ERROR");

        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }

    // Handler riêng cho async (CompletableFuture)
    @ExceptionHandler(CompletionException.class)
    public void handleAsyncException(HttpServletRequest request, CompletionException e) {
        Throwable cause = e.getCause() != null ? e.getCause() : e;
        LOGGER.error("Async exception caught: {}", cause.getMessage(), cause);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handleMultipartException(MultipartException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("❌ Lỗi khi upload file: " + ex.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("❌ File vượt quá kích thước cho phép!");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("🚫 Không có quyền truy cập!");
    }
}
