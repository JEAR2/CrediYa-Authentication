package co.com.crediya.securityports;

import reactor.core.publisher.Mono;

public interface JwtPort {
    Mono<String> generateToken(String email, String role);
    Mono<Boolean> validateToken(String token);
}
