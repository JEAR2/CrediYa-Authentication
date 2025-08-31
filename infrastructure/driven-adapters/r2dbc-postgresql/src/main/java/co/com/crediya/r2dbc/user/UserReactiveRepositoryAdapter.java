package co.com.crediya.r2dbc.user;

import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.r2dbc.entity.UserEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import co.com.crediya.r2dbc.mappers.UserEntityMapper;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        String,
        UserReactiveRepository
> implements UserRepository {
    private final UserEntityMapper userEntityMapper;
    private final RoleRepository roleRepository;
    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper,UserEntityMapper userEntityMapper,RoleRepository roleRepository) {

        super(repository, mapper, d -> mapper.map(d, User.class));
        this.userEntityMapper = userEntityMapper;
        this.roleRepository = roleRepository;
    }


    @Override
    public Mono<User> save(User user){
        UserEntity entity = userEntityMapper.toEntity(user);
        return repository.save(entity)
                .flatMap(saved ->
                        roleRepository.findById(saved.getRoleId())
                                .map(role -> {
                                    User u = userEntityMapper.toDomain(saved);
                                    u.setRole(role);
                                    return u;
                                })
                );

    }
    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .flatMap(entity ->
                        roleRepository.findById(entity.getRoleId())
                                .map(role -> {
                                    User user = userEntityMapper.toDomain(entity);
                                    user.setRole(role);
                                    return user;
                                })
                );
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
