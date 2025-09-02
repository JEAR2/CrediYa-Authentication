package co.com.crediya.api.exception.handler;

import co.com.crediya.api.exceptions.GlobalHandlerException;
import co.com.crediya.api.exceptions.model.ResponseDTO;
import co.com.crediya.exceptions.AuthenticationException;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalHandlerExceptionTest {
    private GlobalHandlerException handler;
    private ObjectMapper mapper;
    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        handler = new GlobalHandlerException(mapper);
    }

    @Test
    void handleAuthenticationExceptionShouldReturnCustomStatus() {
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/users").build());

        AuthenticationException exception = new AuthenticationException( ExceptionStatusCode.BAD_REQUEST, "Custom Error", HttpStatus.BAD_REQUEST.value());

        Mono<Void> result = handler.handle(exchange, exception);

        StepVerifier.create(result).verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exchange.getResponse().getHeaders().getContentType()).isNotNull();
    }

    @Test
    void handleConstraintViolationExceptionShouldReturnBadRequest() {
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/users").build());

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("fieldName");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be null");

        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);
        ConstraintViolationException exception = new ConstraintViolationException(violations);

        Mono<Void> result = handler.handle(exchange, exception);

        StepVerifier.create(result).verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleUnknownExceptionShouldReturnInternalServerError() {
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/users").build());

        RuntimeException exception = new RuntimeException("Unexpected error");
        Mono<Void> result = handler.handle(exchange, exception);

        StepVerifier.create(result).verifyComplete();
        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
/*
    @Test
    void buildFailureResponseShouldHandleObjectMapperException() throws Exception {
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());

        when(mapper.writeValueAsBytes(any())).thenThrow(new JsonProcessingException("Failed") {});

        ResponseDTO<String> error = new ResponseDTO<>(LocalDateTime.now(),"","","", List.of("error"));

        StepVerifier.create(handler.buildFailureResponse(exchange, HttpStatus.BAD_REQUEST, error))
                .verifyComplete();

        // Verificamos que onErrorResume ejecutó correctamente y seteó INTERNAL_SERVER_ERROR
        assert exchange.getResponse().getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR;
        assert exchange.getResponse().getHeaders().getContentType() == null ||
                exchange.getResponse().getHeaders().getContentType().equals(MediaType.APPLICATION_JSON);
    }*/
}
