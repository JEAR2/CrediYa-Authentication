package co.com.crediya.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    LocalDate birthDate;
    String address;
    String email;
    String identityDocument;
    String phoneNumber;
    Integer roleId;
    BigDecimal baseSalary;

}
