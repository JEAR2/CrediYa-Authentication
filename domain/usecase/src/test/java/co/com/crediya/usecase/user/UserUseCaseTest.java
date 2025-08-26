package co.com.crediya.usecase.user;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Calendar;

import static org.mockito.Mockito.*;


class UserUseCaseTest {
    private UserRepository userRepository;
    private UserUseCase userUseCase;

    private User createValidUser() {
        Calendar dateBirth = Calendar.getInstance();
        dateBirth.set(1990, Calendar.JANUARY, 15);

        return new User().toBuilder()
                .id("1")
                .name("John")
                .lastName("Doe")
                .address("Calle2")
                .email("john.doe@example.com")
                .birthDate(dateBirth.getTime())
                .phoneNumber("123456789")
                .roleId(1)
                .identityDocument("12345678")
                .baseSalary(1000000.0)
                .build();
    }

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userUseCase = new UserUseCase(userRepository);
    }

    @Test
    void saveSuccess() {

        User user = createValidUser();

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(userRepository.save(user)).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.save(user))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository, Mockito.times(1)).existsByEmail(user.getEmail());
        verify(userRepository, Mockito.times(1)).save(user);

    }
    @Test
    void saveEmailExists() {

        User user = createValidUser();

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(true));
        when(userRepository.save(user)).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.save(user))
                .expectErrorMessage("El correo electrónico ya está registrado")
                .verify();

        verify(userRepository, Mockito.times(1)).existsByEmail(user.getEmail());
        verify(userRepository, never()).save(any());

    }
    @Test
    void testSaveInvalidUser() {
        Calendar dateBirth = Calendar.getInstance();
        dateBirth.set(1990, Calendar.JANUARY, 15);
        User user = new User().toBuilder()
                .id("1")
                .name("")
                .lastName("Acevedo")
                .address("Calle2")
                .email("a@a.com")
                .birthDate(dateBirth.getTime())
                .phoneNumber("123456789")
                .roleId(1)
                .identityDocument("jeep")
                .baseSalary(15000.0)
                .build();

        StepVerifier.create(userUseCase.save(user))
                .expectErrorMessage("El nombre no puede estar vacío")
                .verify();

        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository, never()).save(any());
    }


    @Test
    void findByEmailFind() {
        User user = createValidUser();
        when(userRepository.findByEmail("a@a.com"))
                .thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.findByEmail("a@a.com"))
                .expectNext(user)
                .verifyComplete();
    }
    @Test
    void findByEmailNotFound() {
        when(userRepository.findByEmail("a@a.com"))
                .thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.findByEmail("a@a.com"))
                .verifyComplete();
    }

    @Test
    void existsByEmailTrue() {
        when(userRepository.existsByEmail("a@a.com"))
                .thenReturn(Mono.just(true));

        StepVerifier.create(userUseCase.existsByEmail("a@a.com"))
                .expectNext(true)
                .verifyComplete();
    }
    @Test
    void existsByEmailFalse() {
        when(userRepository.existsByEmail("a@a.com"))
                .thenReturn(Mono.just(false));

        StepVerifier.create(userUseCase.existsByEmail("a@a.com"))
                .expectNext(false)
                .verifyComplete();
    }
}