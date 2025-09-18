package co.com.crediya.security.config;

import co.com.crediya.exceptions.AuthenticationUnauthorizedException;
import co.com.crediya.security.enums.SecurityConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtKeyConfig {

    @Value("${security.jwt.private-key-location}")
    private String privateKeyResource;

    @Value("${security.jwt.public-key-location}")
    private String publicKeyResource;

    @Bean
    public Mono<PrivateKey> privateKey() {

        return  Mono.fromCallable( () -> {
                    String key = privateKeyResource
                            .replaceAll(SecurityConstants.REGEX_START_PRIVATE_KEY.getValue(), "")
                            .replaceAll(SecurityConstants.REGEX_END_PRIVATE_KEY.getValue(), "")
                            .replaceAll(SecurityConstants.REGEX_SPACES.getValue(), "");

                    byte[] decoded = Base64.getDecoder().decode(key);
                    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
                    return KeyFactory.getInstance(SecurityConstants.TYPE_ALGORITHM.getValue()).generatePrivate(spec);
                })
                .onErrorResume( error -> Mono.error(new AuthenticationUnauthorizedException(error.getMessage())) );
    }

    @Bean
    public Mono<PublicKey> publicKey(){
        return  Mono.fromCallable( () -> {
                    String key = publicKeyResource
                            .replaceAll(SecurityConstants.REGEX_START_PUBLIC_KEY.getValue(), "")
                            .replaceAll(SecurityConstants.REGEX_END_PUBLIC_KEY.getValue(), "")
                            .replaceAll(SecurityConstants.REGEX_SPACES.getValue(), "");

                    byte[] decoded = Base64.getDecoder().decode(key);
                    X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
                    return KeyFactory.getInstance(SecurityConstants.TYPE_ALGORITHM.getValue()).generatePublic(spec);
                })
                .onErrorResume( error -> Mono.error(new AuthenticationUnauthorizedException(error.getMessage())) );
    }
}
