package co.com.crediya.r2dbc.user;

import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @InjectMocks
    UserReactiveRepositoryAdapter userReactiveRepositoryAdapter;

    @Mock
    UserReactiveRepository userRepository;

    @Mock
    ObjectMapper mapper;

    private User user;

    private UserEntity userEntityOne;

    @BeforeEach
    void setUp() {
        Calendar dateBirth = Calendar.getInstance();
        dateBirth.set(1990, Calendar.JANUARY, 15);
        user = User.builder()
                .name("John")
                .lastName("Doe")
                .email("john@doe.com")
                .address("street")
                .birthDate(LocalDate.now())
                .phoneNumber("312009212")
                .baseSalary( new BigDecimal(10) )
                .build();

        userEntityOne = UserEntity.builder()
                .id("1")
                .name("John")
                .lastName("Doe")
                .email("john@doe.com")
                .address("street")
                .birthDate(LocalDate.now())
                .phoneNumber("312009212")
                .baseSalary( new BigDecimal(10) )
                .build();


    }

    @Test
    @DisplayName("Must save user successfully")
    void saveUserWhenUserCorrect_ShouldSavedUser() {
        // Arrange
        when(userRepository.save(userEntityOne)).thenReturn(Mono.just(userEntityOne));
        when(mapper.map(userEntityOne, User.class)).thenReturn(user);
        when(mapper.map(user, UserEntity.class)).thenReturn(userEntityOne);
        // Act
        Mono<User> result = userReactiveRepositoryAdapter.save(user);
        // Assert
        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findByEmail_WhenUserExists_ShouldReturnMappedEntity() {
        // Arrange
        when(userRepository.findByEmail( userEntityOne.getEmail() ))
                .thenReturn( Mono.just(userEntityOne) );

        when(mapper.map(userEntityOne, User.class)).thenReturn(user);
        // Act
        Mono<User> result = userReactiveRepositoryAdapter.findByEmail(userEntityOne.getEmail());
        // Assert
        StepVerifier.create(result)
                .expectNextMatches( userFound -> userFound.getEmail().equals(user.getEmail()) )
                .verifyComplete();
    }

    @Test
    void findByEmail_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(userRepository.findByEmail( userEntityOne.getEmail() ))
                .thenReturn( Mono.empty() );
        // Act
        Mono<User> result = userReactiveRepositoryAdapter.findByEmail(userEntityOne.getEmail());

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(userRepository, times(1)).findByEmail(userEntityOne.getEmail());
    }

    @Test
    void existsByEmail_WhenEmailExists_ShouldReturnTrue() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = userReactiveRepositoryAdapter.existsByEmail(email);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(userRepository, times(1)).existsByEmail(email);
    }
    @Test
    void existsByEmail_WhenEmailDoesNotExist_ShouldReturnFalse() {
        // Arrange
        String email = "notfound@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(Mono.just(false));

        // Act
        Mono<Boolean> result = userReactiveRepositoryAdapter.existsByEmail(email);

        // Assert
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(userRepository, times(1)).existsByEmail(email);
    }
}