package co.com.crediya.usecase.role;

import co.com.crediya.exceptions.AuthenticationBadRequestException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.RoleRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ValidateRoleUseCase implements ValidateRoleUseCasePort{
    private final RoleRepository roleRepository;

    @Override
    public Mono<Role> findByName(String roleName) {
        return roleRepository.findByName(roleName).switchIfEmpty(Mono.error(
                new AuthenticationBadRequestException(
                        String.format(ExceptionMessages.ROLE_NOT_FOUND.getMessage(), roleName)
                )
        ));
    }
}
