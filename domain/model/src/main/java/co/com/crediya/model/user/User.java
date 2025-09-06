package co.com.crediya.model.user;
import co.com.crediya.model.role.Role;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString(exclude = "password")
public class User {
    String id;
    String name;
    String lastName;
    LocalDate birthDate;
    String address;
    String email;
    String identityDocument;
    String phoneNumber;
    Double baseSalary;
    Role role;
    String password;

}
