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

    @InjectMocks
    private RoleReactiveRepositoryAdapter roleReactiveRepositoryAdapter;

    @Test
    void findById_WhenRoleExists_ShouldReturnMappedRole() {
        // Arrange
        RoleEntity entity = new RoleEntity(1, "ADMIN", "Admin role");
        Role role = new Role("1", "ADMIN", "Admin role");

        when(roleReactiveRepository.findById("1")).thenReturn(Mono.just(entity));
        when(objectMapper.map(entity, Role.class)).thenReturn(role);

        // Act + Assert
        StepVerifier.create(roleReactiveRepositoryAdapter.findById(1))
                .expectNextMatches(found ->
                        found.getId().equals(role.getId()) &&
                                found.getName().equals(role.getName()) &&
                                found.getDescription().equals(role.getDescription())
                )
                .verifyComplete();

        verify(roleReactiveRepository, times(1)).findById("1");
        verify(objectMapper, times(1)).map(entity, Role.class);
    }

    @Test
    void findById_WhenRoleDoesNotExist_ShouldReturnEmptyMono() {
        // Arrange
        when(roleReactiveRepository.findById("2")).thenReturn(Mono.empty());

        // Act + Assert
        StepVerifier.create(roleReactiveRepositoryAdapter.findById(2))
                .expectNextCount(0)
                .verifyComplete();

        verify(roleReactiveRepository, times(1)).findById("2");
        verifyNoInteractions(objectMapper); // ObjectMapper no se debe llamar
    }

    @Test
    void findByName_WhenRoleExists_ShouldReturnRole() {
        // Arrange
        RoleEntity entity = new RoleEntity(1, "ADMIN", "Admin role");
        Role expectedRole = new Role("1", "ADMIN", "Admin role");
        when(objectMapper.map(entity, Role.class)).thenReturn(expectedRole);
        when(roleReactiveRepository.findByName("ADMIN")).thenReturn(Mono.just(entity));

        // Act + Assert
        StepVerifier.create(roleReactiveRepositoryAdapter.findByName("ADMIN"))
                .expectNextMatches(role ->
                        role.getId().equals(expectedRole.getId()) &&
                                role.getName().equals(expectedRole.getName()) &&
                                role.getDescription().equals(expectedRole.getDescription())
                )
                .verifyComplete();

        verify(roleReactiveRepository, times(1)).findByName("ADMIN");
    }

    @Test
    void findByName_WhenRoleDoesNotExist_ShouldReturnEmptyMono() {
        // Arrange
        when(roleReactiveRepository.findByName("UNKNOWN")).thenReturn(Mono.empty());

        // Act + Assert
        StepVerifier.create(roleReactiveRepositoryAdapter.findByName("UNKNOWN"))
                .expectNextCount(0)
                .verifyComplete();

        verify(roleReactiveRepository, times(1)).findByName("UNKNOWN");
    }
}