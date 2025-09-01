package co.com.crediya.api.config;

import co.com.crediya.api.dtos.ResponseUserDTO;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.api.user.UserHandler;
import co.com.crediya.api.user.RouterRest;
import co.com.crediya.api.util.ValidatorUtil;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.ports.TransactionManagement;
import co.com.crediya.securityports.JwtPort;
import co.com.crediya.usecase.auth.LoginUseCase;
import co.com.crediya.usecase.role.ValidateRoleUseCasePort;
import co.com.crediya.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, UserHandler.class, PathsConfig.class, ValidatorUtil.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ValidateRoleUseCasePort validateRoleUseCasePort;

    @MockitoBean
    private TransactionManagement transactionManagement;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private UserDTOMapper userDTOMapper;

    @MockitoBean
    private LoginUseCase loginUseCase;

    @MockitoBean
    private JwtPort jwtPort;



    private final User user = User.builder()
            .id("1")
            .name("jear")
            .lastName("acevedo")
            .email("a@a.com")
            .birthDate(LocalDate.now())
            .address("dir")
            .identityDocument("2323")
            .phoneNumber("131231")
            .role(new Role("1","ADMIN","Description"))
            .baseSalary(new BigDecimal("12000.0"))
            .build();

    private final ResponseUserDTO userResponse = new ResponseUserDTO("1","juan","acevedo",new Date(),"dir","a@a.com","15486","2323",1,new BigDecimal("12000.0"));


    @BeforeEach
    void setUp() {
        Role role = new Role("1", "ROLE_ADMIN", "description");
        when(userUseCase.existsByEmail(user.getEmail())).thenReturn(Mono.just(true));
        when( userDTOMapper.toResponseDTO(any()) ).thenReturn(userResponse);
        when(validateRoleUseCasePort.findByName("ROLE_ADMIN"))
                .thenReturn(Mono.just(role));
    }

    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.get()
                .uri("/api/v1/users/exists/{email}","a@a.com")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}