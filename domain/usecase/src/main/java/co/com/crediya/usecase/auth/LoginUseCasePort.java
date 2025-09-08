package co.com.crediya.usecase.auth;

import co.com.crediya.model.user.User;
import reactor.core.publisher.Mono;

public interface LoginUseCasePort {
    Mono<User> login(String email, String password);
}
