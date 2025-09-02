package co.com.crediya.security.helper;

import co.com.crediya.securityports.JwtPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtAdapter implements JwtPort {

    private final PrivateKey privateKey;
    private final RSAPublicKey publicKey;

    @Override
    public Mono<String> generateToken(String email, String role) {
        return Mono.fromCallable(() ->Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15 minutos
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact());
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> {
            Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }).onErrorReturn(false);
    }
}
