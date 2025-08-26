package co.com.crediya.api.user;

import co.com.crediya.api.dtos.CreateUserDTO;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.usecase.user.UserUseCase;
import co.com.crediya.usecase.user.UserUseCasePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {

    private final UserUseCasePort userUseCase;
    private final UserDTOMapper userDTOMapper;
    private final TransactionalOperator  transactionalOperator;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateUserDTO.class)
                .map(userDTOMapper::toModel)
                .doOnNext(user -> log.debug("Usuario recibido: {}", user))
                .flatMap(userUseCase::save)
                .doOnSuccess(savedUser -> log.info("Usuario guardado con id={}", savedUser.getId()))
                .doOnError(err -> log.error("Error guardando usuario: {}", err.getMessage(), err))
                .map(userDTOMapper::toResponseDTO)
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser))
                .as(transactionalOperator::transactional);
    }

    public Mono<ServerResponse> listenFindByEmail(ServerRequest request) {
        String email = request.pathVariable("email");

        return userUseCase.existsByEmail(email)
                .flatMap(exists -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(exists));
    }

}
