package co.com.crediya.security.exception.handler;

import co.com.crediya.exceptions.AuthenticationForbiddenException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AccessDeniedExceptionHandler  implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return Mono.error( new AuthenticationForbiddenException(ExceptionMessages.DO_NOT_ACCESS_RESOURCE.getMessage()));
    }
}
