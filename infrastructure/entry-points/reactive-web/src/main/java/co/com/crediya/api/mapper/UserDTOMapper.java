package co.com.crediya.api.mapper;

import co.com.crediya.api.dtos.CreateUserDTO;
import co.com.crediya.api.dtos.ResponseUserDTO;
import co.com.crediya.model.user.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {
    ResponseUserDTO toResponseDTO(User user);
    List<ResponseUserDTO> toResponseDTOs(List<User> users);
    User toModel(CreateUserDTO createUserDTO);
}
