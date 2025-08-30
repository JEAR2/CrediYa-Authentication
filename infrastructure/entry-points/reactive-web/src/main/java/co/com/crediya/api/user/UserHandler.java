package co.com.crediya.api.user;

import co.com.crediya.api.dtos.CreateUserDTO;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.api.util.HandlersResponseUtil;
import co.com.crediya.api.util.ValidatorUtil;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.ports.TransactionManagement;
import co.com.crediya.usecase.user.UserUseCasePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {

    private final UserUseCasePort userUseCase;
    private final UserDTOMapper userDTOMapper;
    private final TransactionManagement transactionManagement;
    private final ValidatorUtil validatorUtil;


    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateUserDTO.class)
                .flatMap( validatorUtil::validate )
                .flatMap(dto -> {
                    var user = userDTOMapper.toModel(dto);
                    return transactionManagement.inTransaction(userUseCase.save(user));
                })
                .map(userDTOMapper::toResponseDTO)
                .flatMap( savedUser ->
                        ServerResponse.created(URI.create(""))
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue( HandlersResponseUtil.buildBodySuccessResponse(ExceptionStatusCode.CREATED.getStatus(), savedUser) )
                );
    }


    public Mono<ServerResponse> listenFindByEmail(ServerRequest request) {
        String email = request.pathVariable("email");

        return userUseCase.existsByEmail(email)
                .flatMap(exists -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(exists));
    }

}
