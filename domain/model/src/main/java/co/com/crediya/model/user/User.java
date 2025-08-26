package co.com.crediya.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    String id;
    String name;
    String lastName;
    Date birthDate;
    String address;
    String email;
    String identityDocument;
    String phoneNumber;
    Integer roleId;
    Double baseSalary;

}
