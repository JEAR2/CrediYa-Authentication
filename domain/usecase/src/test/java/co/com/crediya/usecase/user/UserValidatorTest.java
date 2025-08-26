package co.com.crediya.usecase.user;

import co.com.crediya.model.user.User;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    @Test
    void validateUser() {
        Calendar dateBirth = Calendar.getInstance();
        dateBirth.set(1990, Calendar.JANUARY, 15);

        User validUser = new User().toBuilder()
                .id("1")
                .name("jear")
                .lastName("Acevedo")
                .address("Calle2")
                .email("a@a.com")
                .birthDate(dateBirth.getTime())
                .phoneNumber("123456789")
                .roleId(1)
                .identityDocument("jeep")
                .baseSalary(15.0)
                .build();

        StepVerifier.create(UserValidator.validateUser(validUser))
                .verifyComplete();
    }

    @Test
    void testNameEmpty() {
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
                .baseSalary(15.0)
                .build();

        StepVerifier.create(UserValidator.validateUser(user))
                .expectErrorMessage("El nombre no puede estar vacío")
                .verify();
    }
    @Test
    void testLastNameEmpty() {
        Calendar dateBirth = Calendar.getInstance();
        dateBirth.set(1990, Calendar.JANUARY, 15);
        User user = new User().toBuilder()
                .id("1")
                .name("John")
                .lastName("")
                .address("Calle2")
                .email("a@a.com")
                .birthDate(dateBirth.getTime())
                .phoneNumber("123456789")
                .roleId(1)
                .identityDocument("jeep")
                .baseSalary(15.0)
                .build();

        StepVerifier.create(UserValidator.validateUser(user))
                .expectErrorMessage("El apellido no puede estar vacío")
                .verify();
    }

    @Test
    void testEmailEmpty() {
        Calendar dateBirth = Calendar.getInstance();
        dateBirth.set(1990, Calendar.JANUARY, 15);
        User user = new User().toBuilder()
                .id("1")
                .name("John")
                .lastName("Acevedo")
                .address("Calle2")
                .email("")
                .birthDate(dateBirth.getTime())
                .phoneNumber("123456789")
                .roleId(1)
                .identityDocument("jeep")
                .baseSalary(15.0)
                .build();

        StepVerifier.create(UserValidator.validateUser(user))
                .expectErrorMessage("El correo electrónico es obligatorio")
                .verify();
    }

    @Test
    void testEmailInvalid() {
        Calendar dateBirth = Calendar.getInstance();
        dateBirth.set(1990, Calendar.JANUARY, 15);
        User user = new User().toBuilder()
                .id("1")
                .name("John")
                .lastName("Acevedo")
                .address("Calle2")
                .email("a@")
                .birthDate(dateBirth.getTime())
                .phoneNumber("123456789")
                .roleId(1)
                .identityDocument("jeep")
                .baseSalary(15.0)
                .build();

        StepVerifier.create(UserValidator.validateUser(user))
                .expectErrorMessage("El correo electrónico no es válido")
                .verify();
    }

    @Test
    void testSalaryInvalid() {
        Calendar dateBirth = Calendar.getInstance();
        dateBirth.set(1990, Calendar.JANUARY, 15);
        User user = new User().toBuilder()
                .id("1")
                .name("John")
                .lastName("Acevedo")
                .address("Calle2")
                .email("a@a.com")
                .birthDate(dateBirth.getTime())
                .phoneNumber("123456789")
                .roleId(1)
                .identityDocument("jeep")
                .baseSalary(1500000000000.0)
                .build();

        StepVerifier.create(UserValidator.validateUser(user))
                .expectErrorMessage("El salario base debe estar entre 0 y 1.500.000")
                .verify();
    }


}