package co.com.crediya.exceptions;

import co.com.crediya.exceptions.enums.ExceptionStatusCode;

public class AuthenticationException extends RuntimeException {
    private ExceptionStatusCode statusCode;

    public AuthenticationException(ExceptionStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public ExceptionStatusCode getStatusCode() {
        return statusCode;
    }
}
