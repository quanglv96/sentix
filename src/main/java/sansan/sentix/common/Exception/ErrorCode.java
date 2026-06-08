package sansan.sentix.common.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common
    SUCCESS("00", "Success"),
    BAD_REQUEST("400", "Bad request"),
    INVALID_REQUEST("4001", "Invalid request"),
    RESOURCE_NOT_FOUND("4004", "Resource not found"),
    UNKNOWN_ERROR("9999", "Unknown error"),
    SYSTEM_ERROR("5000", "An error occurred"),

    // Authentication
    UNAUTHORIZED("401", "Unauthorized"),
    TOKEN_EXPIRED("4011", "Token expired"),
    INVALID_TOKEN("4012", "Invalid token"),

    // Authorization
    FORBIDDEN("403", "Access denied"),

    // Validation
    VALIDATION_ERROR("422", "Validation error"),
    INVALID_DATA("4221", "Invalid data"),

    // Business
    BUSINESS_ERROR("5001", "Business error"),
    DUPLICATE_DATA("5002", "Data already exists"),
    DATA_NOT_FOUND("5003", "Data not found"),

    // System
    INTERNAL_SERVER_ERROR("500", "Internal server error"),
    DATABASE_ERROR("5004", "Database error"),
    EXTERNAL_SERVICE_ERROR("5005", "External service error"),
    FILE_UPLOAD_ERROR("5006", "File upload failed");

    private final String code;
    private final String message;
}
