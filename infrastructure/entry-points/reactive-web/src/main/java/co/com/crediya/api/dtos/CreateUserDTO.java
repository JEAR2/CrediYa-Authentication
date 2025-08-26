package co.com.crediya.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;
@Schema(description = "Modelo que representa un usuario")
public record CreateUserDTO( @NotBlank @Schema(description = "Nombres del usuario", example = "Juan") String name,
                             @NotBlank @Schema(description = "Apellidos del usuario", example = "Pérez") String lastName,
                             @NotBlank @Schema(description = "Correo electrónico", example = "juan@email.com") String email,
                             @Schema(description = "Fecha de nacimiento", example = "1990-01-01") Date birthDate,
                             @Schema(description = "Dirección del usuario", example = "Calle 123") String address,
                             @Schema(description = "Documento de identidad", example = "123456789") String identityDocument,
                             @Schema(description = "Número de teléfono", example = "3001234567") String phoneNumber,
                             @Schema(description = "ID del rol", example = "1") Integer roleId,
                             @NotNull @Min(0) @Max(15000000) @Schema(description = "Salario base", example = "2500000") BigDecimal baseSalary) {
}
