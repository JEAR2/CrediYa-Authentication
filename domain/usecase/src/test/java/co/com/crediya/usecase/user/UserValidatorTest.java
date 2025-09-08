package co.com.crediya.usecase.user;

import co.com.crediya.exceptions.AuthenticationIllegalArgumentException;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.securityports.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;

import static org.mockito.Mockito.when;

class UserValidatorTest {

    private User createValidUser;
    private PasswordEncoder passwordEncoder;
    @BeforeEach
    void setUp() {
        Calendar dateBirth = Calendar.getInstance();
        dateBirth.set(1990, Calendar.JANUARY, 15);
        createValidUser = new User().toBuilder()
                .id("1")
                .name("John")
                .lastName("Doe")
                .address("Calle2")
                .email("john.doe@example.com")
                .birthDate(LocalDate.now())
                .phoneNumber("123456789")
                .role(new Role().toBuilder().id("ROLE_ADMIN").build())
                .identityDocument("12345678")
                .baseSalary(1000000.0)
                .build();


    }



    @Test
    void saveUser_WhenUserIsValid_ShouldPass() {

        StepVerifier.create(UserValidator.validateUser(createValidUser))
                .verifyComplete();
    }

    @Test
    void saveUser_WhenNameIsEmpty_ShouldError() {

        createValidUser.setName("");
        StepVerifier.create(UserValidator.validateUser(createValidUser))
                .expectError(AuthenticationIllegalArgumentException.class)
                .verify();
    }
    @Test
    void saveUser_WhenNameIsNull_ShouldError() {

        createValidUser.setName(null);
        StepVerifier.create(UserValidator.validateUser(createValidUser))
                .expectError(AuthenticationIllegalArgumentException.class)
                .verify();
    }
    @Test
    void saveUser_WhenLastNameIsEmpty_ShouldError() {

        createValidUser.setLastName("");
        StepVerifier.create(UserValidator.validateUser(createValidUser))
                .expectError(AuthenticationIllegalArgumentException.class)
                .verify();
    }

    @Test
    void saveUser_WhenLastNameIsNull_ShouldError() {

        createValidUser.setLastName(null);
        StepVerifier.create(UserValidator.validateUser(createValidUser))
                .expectError(AuthenticationIllegalArgumentException.class)
                .verify();
    }


    @Test
    void saveUser_WhenEmailIsEmpty_ShouldError() {

        createValidUser.setEmail("");
        StepVerifier.create(UserValidator.validateUser(createValidUser))
                .expectError(AuthenticationIllegalArgumentException.class)
                .verify();
    }
    @Test
    void saveUser_WhenEmailIsNull_ShouldError() {

        createValidUser.setEmail(null);
        StepVerifier.create(UserValidator.validateUser(createValidUser))
                .expectError(AuthenticationIllegalArgumentException.class)
                .verify();
    }

    @Test
    void saveUser_WhenEmailIsInvalid_ShouldError() {
        createValidUser.setEmail("aaa");

        StepVerifier.create(UserValidator.validateUser(createValidUser))
                .expectError(AuthenticationIllegalArgumentException.class)
                .verify();
    }

    @Test
    void saveUser_WhenSalaryIsNull_ShouldError() {
        createValidUser.setBaseSalary(null);

        StepVerifier.create(UserValidator.validateUser(createValidUser))
                .expectError(AuthenticationIllegalArgumentException.class)
                .verify();
    }

    @Test
    void saveUser_WhenSalaryIsZero_ShouldError() {
        createValidUser.setBaseSalary(0.0);

        StepVerifier.create(UserValidator.validateUser(createValidUser))
                .expectError(AuthenticationIllegalArgumentException.class)
                .verify();
    }

    @Test
    void saveUser_WhenSalaryExceedsMax_ShouldError() {
        createValidUser.setBaseSalary(15000000000000.0);

        StepVerifier.create(UserValidator.validateUser(createValidUser))
                .expectError(AuthenticationIllegalArgumentException.class)
                .verify();
    }


    @Test
    void saveUser_WhenSalaryIsInvalid_ShouldError() {
        createValidUser.setBaseSalary(-1.0);

        StepVerifier.create(UserValidator.validateUser(createValidUser))
                .expectError(AuthenticationIllegalArgumentException.class)
                .verify();
    }



}