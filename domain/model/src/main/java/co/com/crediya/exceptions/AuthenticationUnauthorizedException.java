package co.com.crediya.exceptions;

import co.com.crediya.exceptions.enums.ExceptionStatusCode;

public class AuthenticationUnauthorizedException extends AuthenticationException {

    public AuthenticationUnauthorizedException(String message) {
        super(ExceptionStatusCode.UNAUTHORIZED, message,401);
    }
}
