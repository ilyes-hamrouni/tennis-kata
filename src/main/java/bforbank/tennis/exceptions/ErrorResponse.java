package bforbank.tennis.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int errorCode;
    private final String errorMessage;
    private final String path;

    public ErrorResponse(int errorCode, String errorMessage, String path) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.path = path;
    }

}
