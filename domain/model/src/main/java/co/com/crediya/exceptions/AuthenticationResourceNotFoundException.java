package co.com.crediya.exceptions;

import co.com.crediya.exceptions.enums.ExceptionStatusCode;

public class AuthenticationResourceNotFoundException extends AuthenticationException {

    public AuthenticationResourceNotFoundException(String message) {
        super(ExceptionStatusCode.NOT_FOUND, message);
    }
}
