package co.com.crediya.api.dtos;

import java.math.BigDecimal;
import java.util.Date;

public record ResponseUserDTO(String name,
                              String lastName,
                              Date birthDate,
                              String address,
                              String email,
                              String identityDocument,
                              String phoneNumber,
                              BigDecimal baseSalary) {
}
