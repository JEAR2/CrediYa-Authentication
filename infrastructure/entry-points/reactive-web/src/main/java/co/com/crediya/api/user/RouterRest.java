package co.com.crediya.api.user;

import co.com.crediya.api.config.PathsConfig;
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

    private final PathsConfig pathsConfig;
    private final UserHandler userHandler;
    @Bean
    @RouterOperations({
            @RouterOperation(path = "/api/v1/users", produces = {MediaType.APPLICATION_JSON_VALUE,}, method = RequestMethod.POST, beanClass = UserHandler.class, beanMethod = "listenSaveUser"),
            @RouterOperation(path = "/api/v1/users/login", produces = {MediaType.APPLICATION_JSON_VALUE,}, method = RequestMethod.POST, beanClass = UserHandler.class, beanMethod = "login")


    })
    public RouterFunction<ServerResponse> routerFunction(UserHandler userHandler) {
        return route(POST(pathsConfig.getUsers()), this.userHandler::listenSaveUser)
                .andRoute(GET(pathsConfig.getEmailExists()), this.userHandler::listenFindByEmail)
                .andRoute(GET(pathsConfig.getUserEmail()), this.userHandler::listenFindUserByEmail)
                .andRoute(POST(pathsConfig.getLogin()), this.userHandler::login);
    }



}
