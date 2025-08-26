package co.com.crediya.exceptions.enums;

public enum ExceptionMessages {
    FIELD_NAME_REQUIRED("El campo nombre es requerido"),
    FIELD_LAST_NAME_REQUIRED("El campo apellido es requerido"),
    FIELD_EMAIL_REQUIRED("El campo email es requerido"),
    FIELD_EMAIL_NOT_VALID("El correo electrónico no es válido"),
    FIELD_PAYMENT_OUT_RANGE("El salario base debe estar entre 0 y 1.500.000"),
    USER_WITH_EMAIL_EXIST("Ya existe un usuario con el email %s registrado");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
