package co.com.crediya.usecase.user;

import co.com.crediya.exceptions.AuthenticationIllegalArgumentException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.model.user.User;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class UserValidator {

    private static final BigDecimal MIN_VALUE_BASE_PAYMENT = BigDecimal.ZERO;
    private static final BigDecimal MAX_VALUE_BASE_PAYMENT = BigDecimal.valueOf(15000000);
    private static final String REGEX_VALID_EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";


    public static Mono<Void> validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            return Mono.error(new AuthenticationIllegalArgumentException(ExceptionMessages.FIELD_NAME_REQUIRED.getMessage()));
        }

        if (user.getLastName() == null || user.getLastName().isBlank()) {
            return Mono.error(new AuthenticationIllegalArgumentException(ExceptionMessages.FIELD_LAST_NAME_REQUIRED.getMessage()));
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return Mono.error(new AuthenticationIllegalArgumentException(ExceptionMessages.FIELD_EMAIL_REQUIRED.getMessage()));
        }

        if (!user.getEmail().matches(REGEX_VALID_EMAIL)) {
            return Mono.error(new AuthenticationIllegalArgumentException(ExceptionMessages.FIELD_EMAIL_NOT_VALID.getMessage()));
        }

        if (user.getBaseSalary() == null || user.getBaseSalary().compareTo(MIN_VALUE_BASE_PAYMENT) <= 0 || user.getBaseSalary().compareTo(MAX_VALUE_BASE_PAYMENT)>0) {
            return Mono.error(new AuthenticationIllegalArgumentException(ExceptionMessages.FIELD_PAYMENT_OUT_RANGE.getMessage()));
        }

        return Mono.empty();
    }
}
