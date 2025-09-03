package co.com.crediya.security.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;


class JwtAdapterTest {

    private JwtAdapter jwtAdapter;

    @BeforeEach
    void setup() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair keyPair = keyPairGen.generateKeyPair();


        Mono<PrivateKey> privateKeyMono = Mono.just(keyPair.getPrivate());
        Mono<PublicKey> publicKeyMono = Mono.just(keyPair.getPublic());

        jwtAdapter = new JwtAdapter(privateKeyMono, publicKeyMono);
        ReflectionTestUtils.setField(jwtAdapter, "expiration", 3600000);

    }

    @Test
    void testGenerateAndValidateToken() {
        StepVerifier.create(jwtAdapter.generateToken("a@a.com", "ADMIN"))
                .assertNext(token -> {
                    org.junit.jupiter.api.Assertions.assertNotNull(token);

                    StepVerifier.create(jwtAdapter.validateToken(token))
                            .expectNext(true)
                            .verifyComplete();
                })
                .verifyComplete();
    }

}