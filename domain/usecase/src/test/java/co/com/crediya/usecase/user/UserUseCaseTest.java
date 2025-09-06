package co.com.crediya.usecase.user;

import co.com.crediya.exceptions.AuthenticationBadRequestException;
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


class UserUseCaseTest {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserUseCase userUseCase;

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
        userUseCase = new UserUseCase(userRepository,passwordEncoder);
    }

    @Test
    void saveUser_WhenUserIsValid_ShouldSaveSuccessfully() {

        User user = createValidUser();
        User userWithHashedPassword = user.toBuilder().password("hashed1234").build();

        // Mock de repositorio
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(userWithHashedPassword));

        // Mock de passwordEncoder
        when(passwordEncoder.encode(user.getPassword())).thenReturn(Mono.just("hashed1234"));

        StepVerifier.create(userUseCase.save(user))
                .expectNext(userWithHashedPassword)
                .verifyComplete();

        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userRepository, times(1)).save(any(User.class));

    }
    @Test
    void saveUser_WhenEmailAlreadyExists_ShouldError() {

        User user = createValidUser();

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(true));
        when(userRepository.save(user)).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.save(user))
                .expectError(AuthenticationBadRequestException.class)
                .verify();

        verify(userRepository, Mockito.times(1)).existsByEmail(user.getEmail());
        verify(userRepository, never()).save(any());

    }

    @Test
    void findByEmail_WhenUserExists_ShouldReturnUser() {
        User user = createValidUser();
        when(userRepository.findByEmail("a@a.com"))
                .thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.findByEmail("a@a.com"))
                .expectNext(user)
                .verifyComplete();
    }
    @Test
    void findByEmail_WhenUserDoesNotExist_ShouldReturnEmpty() {
        when(userRepository.findByEmail("a@a.com"))
                .thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.findByEmail("a@a.com"))
                .verifyComplete();
    }

    @Test
    void existsByEmail_WhenUserExists_ShouldReturnTrue() {
        when(userRepository.existsByEmail("a@a.com"))
                .thenReturn(Mono.just(true));

        StepVerifier.create(userUseCase.existsByEmail("a@a.com"))
                .expectNext(true)
                .verifyComplete();
    }
    @Test
    void existsByEmail_WhenUserDoesNotExist_ShouldReturnFalse() {
        when(userRepository.existsByEmail("a@a.com"))
                .thenReturn(Mono.just(false));

        StepVerifier.create(userUseCase.existsByEmail("a@a.com"))
                .expectNext(false)
                .verifyComplete();
    }
}