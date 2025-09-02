package co.com.crediya.security.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;


class JwtAdapterTest {

    private JwtAdapter jwtAdapter;

    @BeforeEach
    void setup() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair keyPair = keyPairGen.generateKeyPair();

        jwtAdapter = new JwtAdapter(keyPair.getPrivate(), (RSAPublicKey) keyPair.getPublic());
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