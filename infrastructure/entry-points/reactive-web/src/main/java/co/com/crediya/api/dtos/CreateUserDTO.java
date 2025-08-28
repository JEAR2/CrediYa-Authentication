package co.com.crediya.api.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
public record CreateUserDTO( @NotBlank String name,
                             @NotBlank String lastName,
                             @NotBlank String email,
                             LocalDate birthDate,
                             String address,
                             String identityDocument,
                             String phoneNumber,
                             Integer roleId,
                             @NotNull @Min(0) @Max(15000000)  BigDecimal baseSalary) {
}
