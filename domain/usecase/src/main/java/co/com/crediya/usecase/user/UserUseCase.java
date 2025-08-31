package co.com.crediya.usecase.user;

import co.com.crediya.securityports.PasswordEncoder;
import co.com.crediya.exceptions.AuthenticationBadRequestException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase implements UserUseCasePort {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Mono<User> save(User user){

        return UserValidator.validateUser(user)
                .then(userRepository.existsByEmail(user.getEmail()))
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(
                        new AuthenticationBadRequestException(
                                String.format(
                                        ExceptionMessages.USER_WITH_EMAIL_EXIST.getMessage(),
                                        user.getEmail(), user.getEmail()
                                )
                        )
                ))
                .flatMap(ignore -> passwordEncoder.encode(user.getPassword())
                        .map(hash -> user.toBuilder().password(hash).build())
                )
                .flatMap(userRepository::save);
    }


    @Override
    public Mono<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public  Mono<Boolean> existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
