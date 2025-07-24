package bforbank.tennis.exceptions;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {

    @GetMapping("/throw")
    public String throwTennisException() {
        throw new TennisException(404, TennisErrorConstants.ERROR_PLAYER_NOT_FOUND);
    }

    @GetMapping("/throw-invalid")
    public String throwWithInvalidCode() {
        throw new TennisException(999, "Invalid HTTP code");
    }
}
