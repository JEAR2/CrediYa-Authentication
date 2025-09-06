package co.com.crediya.usecase.auth;

import co.com.crediya.exceptions.AuthenticationResourceNotFoundException;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.securityports.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;


class LoginUseCaseTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private LoginUseCase loginUseCase;

    private User createValidUser() {


        return new User().toBuilder()
                .id("1")
                .name("John")
                .lastName("Doe")
                .address("Calle2")
                .email("john.doe@example.com")
                .birthDate(LocalDate.now())
                .phoneNumber("123456789")
                .role(new Role("1","ADMIN","Description"))
                .identityDocument("12345678")
                .baseSalary(1000000.0)
                .build();
    }

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        loginUseCase = new LoginUseCase(userRepository,passwordEncoder);
    }

    @Test
    void login_WhenCredentialsAreValid_ShouldReturnUser() {
        User user = createValidUser();
        String rawPassword = "1234";

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.just(user));

        when(passwordEncoder.matches(rawPassword, user.getPassword())).thenReturn(Mono.just(true));

        StepVerifier.create(loginUseCase.login(user.getEmail(), rawPassword))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(passwordEncoder, times(1)).matches(rawPassword, user.getPassword());
    }

    @Test
    void login_WhenPasswordIsInvalid_ShouldReturnError() {
        User user = createValidUser();
        String wrongPassword = "wrong";

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(wrongPassword, user.getPassword())).thenReturn(Mono.just(false));

        StepVerifier.create(loginUseCase.login(user.getEmail(), wrongPassword))
                .expectError(AuthenticationResourceNotFoundException.class)
                .verify();
    }

    @Test
    void login_WhenUserDoesNotExist_ShouldReturnError() {
        String email = "nonexistent@example.com";
        String password = "1234";

        when(userRepository.findByEmail(email)).thenReturn(Mono.empty());

        StepVerifier.create(loginUseCase.login(email, password))
                .expectError(AuthenticationResourceNotFoundException.class)
                .verify();
    }
}