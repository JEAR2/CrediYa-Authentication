package co.com.crediya.api.user;

import co.com.crediya.api.dtos.AuthRequestDTO;
import co.com.crediya.api.dtos.AuthResponseDTO;
import co.com.crediya.api.dtos.CreateUserDTO;
import co.com.crediya.api.dtos.ResponseUserDTO;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.api.util.HandlersResponseUtil;
import co.com.crediya.api.util.ValidatorUtil;
import co.com.crediya.securityports.JwtPort;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.ports.TransactionManagement;
import co.com.crediya.usecase.auth.LoginUseCase;
import co.com.crediya.usecase.role.ValidateRoleUseCasePort;
import co.com.crediya.usecase.user.UserUseCasePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    private final ValidateRoleUseCasePort  validateRoleUseCase;
    private final UserDTOMapper userDTOMapper;
    private final TransactionManagement transactionManagement;
    private final ValidatorUtil validatorUtil;
    private final LoginUseCase loginUseCase;
    private final JwtPort jwtPort;


    @Operation( tags = "Users", operationId = "saveUser", description = "Save a user", summary = "Save a user",
            requestBody = @RequestBody( content = @Content( schema = @Schema( implementation = CreateUserDTO.class ) ) ),
            responses = { @ApiResponse( responseCode = "201", description = "User saved successfully.", content = @Content( schema = @Schema( implementation = ResponseUserDTO.class ) ) ),

            })
    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateUserDTO.class)
                .doOnNext( userRequest -> log.info("Saving user={}", userRequest))
                .flatMap( validatorUtil::validate )
                .flatMap(dto ->
                    validateRoleUseCase.findByName(dto.roleName())
                            .map(role -> {
                                var user = userDTOMapper.toModel(dto);
                                user.setRole(role);
                                return user;
                            }))
                .flatMap(user->
                        transactionManagement.inTransaction(userUseCase.save(user))
                )
                .doOnSuccess(saved -> log.info("User saved successfully: {}", saved))
                .doOnError(error -> log.error("Error saving user: {}", error.getMessage(), error))
                .map(userDTOMapper::toResponseDTO)
                .flatMap( savedUser ->
                        ServerResponse.created(URI.create(""))
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue( HandlersResponseUtil.buildBodySuccessResponse(ExceptionStatusCode.CREATED.getStatus(), savedUser) )
                );
    }


    public Mono<ServerResponse> listenFindByEmail(ServerRequest request) {
        String email = request.pathVariable("email");
        log.info("Checking if user exists with email={}", email);
        return userUseCase.existsByEmail(email)
                .doOnSuccess(exists -> log.info("User with email={} exists={}", email, exists))
                .doOnError(ex -> log.error("Error checking user with email={} - reason={}", email, ex.getMessage(), ex))
                .flatMap(exists -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(exists));
    }
    public Mono<ServerResponse> listenFindUserByEmail(ServerRequest request) {
        String email = request.pathVariable("email");
        log.info("Searching user by email={}", email);
        return userUseCase.findByEmail(email)
                .doOnSuccess(user ->
                        log.info(user != null
                                ? "Found user with email={}"
                                : "No user found with email={}", email)
                )
                .doOnError(ex -> log.error("Error searching user with email={} - reason={}", email, ex.getMessage(), ex))
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user));
    }

    @Operation( tags = "Users", operationId = "saveUser", description = "Login  user", summary = "Login user",
            requestBody = @RequestBody( content = @Content( schema = @Schema( implementation = AuthRequestDTO.class ) ) ),
            responses = { @ApiResponse( responseCode = "201", description = "User login successfully.", content = @Content( schema = @Schema( implementation = AuthRequestDTO.class ) ) ),

            })
    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(AuthRequestDTO.class)
                .flatMap(dto -> loginUseCase.login(dto.getEmail(), dto.getPassword()))
                .doOnSuccess(userLogin -> log.info("Correctly lodged: {}", userLogin))
                .doOnError(error -> log.error("Error logging in: {}", error.getMessage(), error))
                .flatMap(user -> jwtPort.generateToken(user.getEmail(), user.getRole().getName())
                        .map(token -> new AuthResponseDTO(user, token)))
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }




}
