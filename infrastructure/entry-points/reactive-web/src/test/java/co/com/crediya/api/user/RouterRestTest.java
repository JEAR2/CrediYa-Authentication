package co.com.crediya.api.user;

import co.com.crediya.api.config.PathsConfig;
import co.com.crediya.api.dtos.AuthRequestDTO;
import co.com.crediya.api.dtos.CreateUserDTO;
import co.com.crediya.api.dtos.ResponseUserDTO;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.api.util.ValidatorUtil;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.ports.TransactionManagement;
import co.com.crediya.securityports.JwtPort;
import co.com.crediya.usecase.auth.LoginUseCase;
import co.com.crediya.usecase.role.ValidateRoleUseCasePort;
import co.com.crediya.usecase.user.UserUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, UserHandler.class, ValidatorUtil.class})
@EnableConfigurationProperties(PathsConfig.class)
@WebFluxTest
class RouterRestTest {


    private static final String USERS_PATH = "/api/v1/users";
    private static final String USERS_PATH_EMAIL_EXISTS = "/api/v1/users/exists/{email}";
    private static final String USERS_PATH_LOGIN = "/api/v1/users/login";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private LoginUseCase loginUseCase;

    @MockitoBean
    private JwtPort jwtPort;


    @MockitoBean
    private UserDTOMapper userDTOMapper;

    @MockitoBean
    private TransactionManagement transactionManagement;

    @MockitoBean
    private ValidateRoleUseCasePort validateRoleUseCasePort;

    @Autowired
    private PathsConfig pathsConfig;

    private final CreateUserDTO createUserDTO = new CreateUserDTO("John","Acevedo","a@a.com", LocalDate.now(),"dir","12","3123","ROLE_ADMIN",new BigDecimal(15000.0), "12345");


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

    private final ResponseUserDTO userResponse = new ResponseUserDTO("juan","acevedo",new Date(),"dir","a@a.com","15486","2323",new BigDecimal("12000.0"));

    @BeforeEach
    void setupMocks() {
        Role role = new Role("1", "ROLE_ADMIN", "description");
        when(validateRoleUseCasePort.findByName("ROLE_ADMIN"))
                .thenReturn(Mono.just(role));
    }


    @Test
    void shouldLoadUserPathProperties() {
        assertEquals(USERS_PATH, pathsConfig.getUsers());
        assertEquals(USERS_PATH_EMAIL_EXISTS, pathsConfig.getEmailExists());
        assertEquals(USERS_PATH_LOGIN, pathsConfig.getLogin());
    }

    @Test
    @DisplayName("Must save a user successfully")
    void saveUser_WhenRequestIsValid_ShouldReturnUser() {

        when( userUseCase.save(user) ).thenReturn(Mono.just(user));
        when(userDTOMapper.toModel(any(CreateUserDTO.class))).thenReturn(user);
        when(userDTOMapper.toResponseDTO(any(User.class))).thenReturn(userResponse);

        when(transactionManagement.inTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        webTestClient.post()
                .uri(USERS_PATH)
                .bodyValue(createUserDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.code").isEqualTo(ExceptionStatusCode.CREATED.getStatus())
                .jsonPath("$.data.email")
                .value(userResponseResult -> {
                            Assertions.assertThat(userResponseResult).isNotNull();
                        }
                );
    }

    @Test
    @DisplayName("Login exitoso debe retornar usuario y token")
    void login_WhenCredentialsAreValid_ShouldReturnUserAndToken() {
        // Arrange
        AuthRequestDTO loginRequest = new AuthRequestDTO("a@a.com", "12345");
        String fakeToken = "jwt-token";

        when(loginUseCase.login(loginRequest.getEmail(), loginRequest.getPassword()))
                .thenReturn(Mono.just(user));

        when(jwtPort.generateToken(user.getEmail(), user.getRole().getName()))
                .thenReturn(Mono.just(fakeToken));

        // Act & Assert
        webTestClient.post()
                .uri(USERS_PATH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo(user.getEmail())
                .jsonPath("$.token").isEqualTo(fakeToken);
    }


}