package co.com.crediya.r2dbc.mappers;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserEntityMapperTest {

    private UserEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserEntityMapperImpl();
    }

    @Test
    void testToDomainReactive() {
        UserEntity entity = new UserEntity();
        entity.setId("1");
        entity.setEmail("test@example.com");
        entity.setName("John");
        entity.setRoleId(1);

        Mono<User> monoDomain = Mono.just(entity)
                .map(mapper::toDomain);

        StepVerifier.create(monoDomain)
                .expectNextMatches(user ->
                        user.getId().equals(entity.getId()) &&
                                user.getEmail().equals(entity.getEmail()) &&
                                user.getName().equals(entity.getName()) &&
                                user.getRole() == null
                )
                .verifyComplete();
    }

    @Test
    void testToEntityReactive() {
        Role role = new Role();
        role.setId("1");

        User user = new User();
        user.setId("1");
        user.setEmail("test@example.com");
        user.setName("John");
        user.setRole(role);

        Mono<UserEntity> monoEntity = Mono.just(user)
                .map(mapper::toEntity);

        StepVerifier.create(monoEntity)
                .consumeNextWith(entity -> {
                    assertThat(entity.getId()).isEqualTo(user.getId());
                    assertThat(entity.getEmail()).isEqualTo(user.getEmail());
                    assertThat(entity.getName()).isEqualTo(user.getName());
                    assertThat(String.valueOf(entity.getRoleId())).isEqualTo(user.getRole().getId());
                })
                .verifyComplete();
    }
}