package co.com.crediya.usecase.user;

import co.com.crediya.model.user.User;
import reactor.core.publisher.Mono;

public class UserValidator {

    public static Mono<Void> validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            return Mono.error(new IllegalArgumentException("El nombre no puede estar vacío"));
        }

        if (user.getLastName() == null || user.getLastName().isBlank()) {
            return Mono.error(new IllegalArgumentException("El apellido no puede estar vacío"));
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return Mono.error(new IllegalArgumentException("El correo electrónico es obligatorio"));
        }

        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return Mono.error(new IllegalArgumentException("El correo electrónico no es válido"));
        }

        if (user.getBaseSalary() == null || user.getBaseSalary() <= 0 || user.getBaseSalary() > 1_500_000) {
            return Mono.error(new IllegalArgumentException("El salario base debe estar entre 0 y 1.500.000"));
        }

        return Mono.empty();
    }
}
