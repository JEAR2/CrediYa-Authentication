package co.com.crediya.exceptions;

import co.com.crediya.exceptions.enums.ExceptionStatusCode;

public class AuthenticationIllegalArgumentException extends AuthenticationException {
    public AuthenticationIllegalArgumentException(String message) {
        super(ExceptionStatusCode.BAD_REQUEST, message);
    }
}
