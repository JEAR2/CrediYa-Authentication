package co.com.crediya.exceptions;

import co.com.crediya.exceptions.enums.ExceptionStatusCode;

public class AuthenticationForbiddenException extends AuthenticationException {
    public AuthenticationForbiddenException(String message) {
        super(ExceptionStatusCode.FORBIDDEN, message,403);
    }
}
