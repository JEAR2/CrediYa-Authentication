package co.com.crediya.r2dbc.helper.security;

import co.com.crediya.securityports.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class BCryptPasswordEncoderAdapter implements PasswordEncoder {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Override
    public Mono<String> encode(String rawPassword) {
        return Mono.fromCallable(() -> encoder.encode(rawPassword));
    }

    @Override
    public Mono<Boolean> matches(String rawPassword, String encodedPassword) {
        return Mono.fromCallable(() -> encoder.matches(rawPassword, encodedPassword));
    }
}
