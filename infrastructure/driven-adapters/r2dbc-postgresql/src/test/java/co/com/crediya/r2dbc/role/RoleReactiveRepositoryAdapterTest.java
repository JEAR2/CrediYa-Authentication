package co.com.crediya.r2dbc.role;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.r2dbc.entity.RoleEntity;
import co.com.crediya.r2dbc.mappers.UserEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RoleReactiveRepositoryAdapterTest {

    @Mock
    private RoleReactiveRepository roleReactiveRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleReactiveRepositoryAdapter roleReactiveRepositoryAdapter;

    @Test
    void findById_WhenRoleExists_ShouldReturnMappedEntity() {
        // Arrange
        RoleEntity entity = new RoleEntity(1, "ADMIN", "Admin role");
        Role role = new Role("1", "ADMIN", "Admin role");

        when(roleReactiveRepository.findById("1")).thenReturn(Mono.just(entity));
        when(objectMapper.map(entity, Role.class)).thenReturn(role);

        // Act + Assert
        StepVerifier.create(roleReactiveRepositoryAdapter.findById(1))
                .expectNextMatches(found ->
                        found.getId().equals(role.getId()) &&
                                found.getName().equals(role.getName()))
                .verifyComplete();

        verify(roleReactiveRepository, times(1)).findById("1");
        verify(objectMapper, times(1)).map(entity, Role.class);
    }
}