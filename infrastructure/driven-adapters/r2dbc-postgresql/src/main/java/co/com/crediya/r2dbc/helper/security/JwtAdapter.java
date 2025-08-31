package co.com.crediya.r2dbc.helper.security;

import co.com.crediya.securityports.JwtPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtAdapter implements JwtPort {

    private final PrivateKey privateKey;
    private final RSAPublicKey publicKey;

    @Override
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15 minutos
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
