package bforbank.tennis.exceptions;

import lombok.Data;

@Data
public class TennisException extends RuntimeException {

    private final int errorCode;
    private final String errorMessage;

    public TennisException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
