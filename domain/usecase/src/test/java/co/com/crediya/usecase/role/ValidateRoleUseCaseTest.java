package co.com.crediya.usecase.role;

import co.com.crediya.exceptions.AuthenticationBadRequestException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;


class ValidateRoleUseCaseTest {
    private  RoleRepository roleRepository;
    private ValidateRoleUseCase validateRoleUseCase;
    @BeforeEach
    void setUp() {
        roleRepository = Mockito.mock(RoleRepository.class);
        validateRoleUseCase = new ValidateRoleUseCase(roleRepository);
    }

    @Test
    void findByName_WhenRoleExists_ShouldReturnRole() {
        Role role = new Role("1", "ADMIN", "Admin role");
        when(roleRepository.findByName("ADMIN")).thenReturn(Mono.just(role));

        StepVerifier.create(validateRoleUseCase.findByName("ADMIN"))
                .expectNext(role)
                .verifyComplete();

        verify(roleRepository, times(1)).findByName("ADMIN");
    }

    @Test
    void findByName_WhenRoleDoesNotExist_ShouldThrowException() {
        when(roleRepository.findByName("UNKNOWN")).thenReturn(Mono.empty());

        StepVerifier.create(validateRoleUseCase.findByName("UNKNOWN"))
                .expectErrorMatches(throwable ->
                        throwable instanceof AuthenticationBadRequestException &&
                                throwable.getMessage().equals(String.format(ExceptionMessages.ROLE_NOT_FOUND.getMessage(), "UNKNOWN"))
                )
                .verify();

        verify(roleRepository, times(1)).findByName("UNKNOWN");
    }


}