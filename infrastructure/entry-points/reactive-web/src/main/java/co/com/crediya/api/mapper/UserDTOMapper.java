package co.com.crediya.api.mapper;

import co.com.crediya.api.dtos.CreateUserDTO;
import co.com.crediya.api.dtos.ResponseUserDTO;
import co.com.crediya.model.user.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserDTOMapper {
    ResponseUserDTO toResponseDTO(User user);
    User toModel(CreateUserDTO createUserDTO);
}
