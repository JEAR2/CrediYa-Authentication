package co.com.crediya.usecase.auth;

import co.com.crediya.securityports.JwtPort;
import co.com.crediya.securityports.PasswordEncoder;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase implements LoginUseCasePort {

    private final UserRepository userRepository;
    private final JwtPort jwtService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Mono<User> login(String email, String password) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")))
                .flatMap(user ->
                        passwordEncoder.matches(password, user.getPassword())
                                .flatMap(valid -> Boolean.TRUE.equals(valid)
                                        ? Mono.just(user)
                                        : Mono.error(new RuntimeException("Credenciales inválidas"))
                                )
                );
    }



}
