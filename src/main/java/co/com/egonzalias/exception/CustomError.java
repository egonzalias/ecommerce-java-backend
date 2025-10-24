package co.com.egonzalias.exception;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class CustomError extends RuntimeException {
    private final String message;
    private final List<String> details;

    public CustomError(String message) {
        super(message);
        this.message = message;
        this.details = Collections.emptyList();
    }

    public CustomError(String title, List<String> details) {
        super(title);
        this.message = title;
        this.details = details != null ? details : Collections.emptyList();
    }

    @Override
    public String getMessage() {
        return message;
    }

}
