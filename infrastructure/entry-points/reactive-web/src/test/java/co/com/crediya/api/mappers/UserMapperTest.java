package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.CreateUserDTO;
import co.com.crediya.api.dtos.ResponseUserDTO;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.model.user.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UserMapperTest {

    private final UserDTOMapper userMapper = Mappers.getMapper(UserDTOMapper.class);


    private final CreateUserDTO createUserDTO = new CreateUserDTO(
            "john",
            "Acevedo",
            "a@a.com",
            LocalDate.now(),
            "Dir",
            "10900122",
            "210012312",
            "ROLE_ADMIN",
            152000.0,
            "12345"
    );

    private final User user = User.builder().
            name("Julian")
            .lastName("Arevalo")
            .email("arevalo@gmail.com")
            .identityDocument("10900122")
            .phoneNumber("210012312")
            .baseSalary(152000.0)
            .password("3123123123")
            .build();

    @Test
    void testCreateRequestToModel() {
        Mono<User> result = Mono.fromCallable(() -> userMapper.toModel(createUserDTO));

        StepVerifier.create(result)
                .expectNextMatches( userResponse ->
                        userResponse.getName().equals(createUserDTO.name())
                                &&  userResponse.getEmail().equals(createUserDTO.email())
                                &&  userResponse.getIdentityDocument().equals(createUserDTO.identityDocument())
                                &&  userResponse.getPhoneNumber().equals(createUserDTO.phoneNumber())
                                &&  userResponse.getBaseSalary().equals(createUserDTO.baseSalary())
                                && userResponse.getLastName().equals(createUserDTO.lastName())
                )
                .verifyComplete();
    }


    @Test
    void testModelToResponse() {
        Mono<ResponseUserDTO> result = Mono.fromCallable(() -> userMapper.toResponseDTO(user));

        StepVerifier.create(result)
                .expectNextMatches( userResponse ->
                        userResponse.name().equals(user.getName())
                )
                .verifyComplete();
    }

}
