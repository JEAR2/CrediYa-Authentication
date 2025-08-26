package co.com.crediya.api.dtos;

import java.util.Date;

public record ResponseUserDTO(String id,
                              String name,
                              String lastName,
                              Date birthDate,
                              String address,
                              String email,
                              String identityDocument,
                              String phoneNumber,
                              Integer roleId,
                              Double baseSalary) {
}
