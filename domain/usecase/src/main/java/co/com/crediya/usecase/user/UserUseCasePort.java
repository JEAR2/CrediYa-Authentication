package co.com.crediya.usecase.user;

import co.com.crediya.model.user.User;
import reactor.core.publisher.Mono;

public interface UserUseCasePort {
    Mono<User> save(User user);
    Mono<User> findByEmail(String email);
    Mono<Boolean> existsByEmail(String email);

}
