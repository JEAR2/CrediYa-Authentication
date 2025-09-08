package co.com.crediya.exceptions;

import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {
    private final ExceptionStatusCode statusCode;
    private final int status;

    public AuthenticationException(ExceptionStatusCode statusCode, String message, int status) {
        super(message);
        this.statusCode = statusCode;
        this.status = status;
    }

}
