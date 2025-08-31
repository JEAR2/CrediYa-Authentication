package co.com.crediya.api.dtos;

import co.com.crediya.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String email;
    private String name;

    public AuthResponseDTO(User user, String token) {
        this.token = token;
        this.email = user.getEmail();
        this.name = user.getName();
    }
}

