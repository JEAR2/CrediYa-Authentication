package co.com.crediya.api.user;

import co.com.crediya.api.config.UserPath;
import co.com.crediya.api.dtos.CreateUserDTO;
import co.com.crediya.api.dtos.ResponseUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final UserPath userPath;
    private final UserHandler userHandler;
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/users",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE,
                    },
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "listenSaveUser",
                    operation = @Operation( tags = "Users", operationId = "saveUser", description = "Save a user", summary = "Save a user",
                            requestBody = @RequestBody( content = @Content( schema = @Schema( implementation = CreateUserDTO.class ) ) ),
                            responses = { @ApiResponse( responseCode = "201", description = "User saved successfully.", content = @Content( schema = @Schema( implementation = ResponseUserDTO.class ) ) ),
                                    //@ApiResponse( responseCode = "400", description = "Request body is not valid.", content = @Content( schema = @Schema( implementation = CustomError.class ) ) )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(UserHandler userHandler) {
        return route(POST(userPath.getUsers()), this.userHandler::listenSaveUser)
                .andRoute(GET("/api/v1/users/exists/{email}"), this.userHandler::listenFindByEmail);
    }



}
