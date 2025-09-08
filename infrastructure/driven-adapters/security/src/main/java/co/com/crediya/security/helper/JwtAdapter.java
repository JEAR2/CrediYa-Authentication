package co.com.crediya.security.helper;

import co.com.crediya.exceptions.AuthenticationUnauthorizedException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.security.enums.SecurityConstants;
import co.com.crediya.securityports.JwtPort;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtAdapter implements JwtPort {

    private final Mono<PrivateKey> privateKeyMono;
    private final Mono<PublicKey> publicKeyMono;

    @Value("${security.jwt.expiration}")
    private Integer expiration;

    @Override
    public Mono<String> generateToken(String email, String role) {
        return privateKeyMono.flatMap(privateKey ->
                Mono.fromCallable(() -> Jwts.builder()
                        .setSubject(email)
                        .claim(SecurityConstants.ROLE_CLAIM.getValue(), role)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + expiration))
                        .signWith(privateKey, SignatureAlgorithm.RS256)
                        .compact())
        );
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        return publicKeyMono.flatMap(publicKey ->
                Mono.fromCallable(() -> {
                            Jwts.parserBuilder()
                                    .setSigningKey(publicKey)
                                    .build()
                                    .parseClaimsJws(token);
                            return true;
                        })
                        .onErrorMap(ExpiredJwtException.class,
                                ex -> new AuthenticationUnauthorizedException(ExceptionMessages.EXPIRED_TOKEN.getMessage()))
                        .onErrorMap(JwtException.class,
                                ex -> new AuthenticationUnauthorizedException(ExceptionMessages.UNAUTHORIZED_SENT_TOKEN_INVALID.getMessage()))
        );
    }
}
