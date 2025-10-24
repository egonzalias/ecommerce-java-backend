package co.com.egonzalias.exception;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ApiError {
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;
    private final String details;
    private Map<String, String> errors;

    public ApiError(int status, String message, String details) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.details = details;
    }

}
