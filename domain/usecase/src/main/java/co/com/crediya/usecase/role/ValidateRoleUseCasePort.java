package co.com.crediya.usecase.role;

import co.com.crediya.model.role.Role;
import reactor.core.publisher.Mono;

public interface ValidateRoleUseCasePort {
    Mono<Role> findByName(String roleName);
}
