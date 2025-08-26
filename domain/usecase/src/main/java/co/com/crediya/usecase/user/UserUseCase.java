package co.com.crediya.usecase.user;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> save(User user){

        return UserValidator.validateUser(user)
                .then(Mono.defer(()->userRepository.existsByEmail(user.getEmail())))
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new IllegalArgumentException("El correo electrónico ya está registrado"));
                    }
                    return userRepository.save(user);
                });
    }

    public Mono<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public  Mono<Boolean> existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
