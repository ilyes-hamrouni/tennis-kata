package bforbank.tennis.exceptions;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TennisException.class)
    public ResponseEntity<ErrorResponse> handleTennisException(TennisException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(ex.getErrorCode(), ex.getErrorMessage(), request.getRequestURI());
        HttpStatus status = HttpStatus.resolve(ex.getErrorCode());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
         return ResponseEntity
                .status(status)
                .body(error);

    }

}

