package co.com.crediya.security.helper;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class BCryptPasswordEncoderAdapterTest {
    private final BCryptPasswordEncoderAdapter adapter = new BCryptPasswordEncoderAdapter();

    @Test
    void testEncodeAndMatches() {
        String raw = "password";

        StepVerifier.create(adapter.encode(raw))
                .assertNext(encoded -> {
                    assertNotNull(encoded);
                    StepVerifier.create(adapter.matches(raw, encoded))
                            .expectNext(true)
                            .verifyComplete();
                })
                .verifyComplete();
    }

    @Test
    void testMatchesFails() {
        StepVerifier.create(adapter.matches("raw", "wrong"))
                .expectNext(false)
                .verifyComplete();
    }
}