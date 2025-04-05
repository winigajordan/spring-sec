package co.jordan.usermicroservices.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UsernameAlreadyExistException extends RuntimeException {
    private String message;

    public UsernameAlreadyExistException(String message) {
        super(message);
    }

}
