package co.com.crediya.api.user;

import co.com.crediya.api.config.PathsConfig;
import co.com.crediya.api.dtos.CreateUserDTO;
import co.com.crediya.api.dtos.ResponseUserDTO;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.api.util.ValidatorUtil;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.model.user.User;
import co.com.crediya.ports.TransactionManagement;
import co.com.crediya.usecase.user.UserUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
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

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private UserDTOMapper userDTOMapper;

    @MockitoBean
    private TransactionManagement transactionManagement;

    @Autowired
    private PathsConfig pathsConfig;

    private final CreateUserDTO createUserDTO = new CreateUserDTO("John","Acevedo","a@a.com", LocalDate.now(),"dir","12","3123",1,new BigDecimal(15000.0));


    private final User user = User.builder()
            .id("1")
            .name("jear")
            .lastName("acevedo")
            .email("a@a.com")
            .birthDate(LocalDate.now())
            .address("dir")
            .identityDocument("2323")
            .phoneNumber("131231")
            .roleId(1)
            .baseSalary(new BigDecimal("12000.0"))
            .build();

    private final ResponseUserDTO userResponse = new ResponseUserDTO("1","juan","acevedo",new Date(),"dir","a@a.com","15486","2323",1,new BigDecimal("12000.0"));



    @Test
    void shouldLoadUserPathProperties() {
        assertEquals(USERS_PATH, pathsConfig.getUsers());
        assertEquals(USERS_PATH_EMAIL_EXISTS, pathsConfig.getEmailExists());
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

}