package co.com.crediya.exceptions;

import co.com.crediya.exceptions.enums.ExceptionStatusCode;

public class AuthenticationBadRequestException extends AuthenticationException {
    public AuthenticationBadRequestException(String message) {
        super(ExceptionStatusCode.BAD_REQUEST, message,400);
    }
}
