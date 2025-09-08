package co.com.crediya.r2dbc.mappers;

import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    @Mapping(target = "role", ignore = true)
    User toDomain(UserEntity entity);

    @Mapping(target = "roleId", source = "role.id")
    UserEntity toEntity(User user);
}
