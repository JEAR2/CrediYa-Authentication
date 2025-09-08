package co.com.crediya.exceptions.enums;

public enum ExceptionMessages {
    FIELD_NAME_REQUIRED("The name field is required."),
    FIELD_LAST_NAME_REQUIRED("The last name field is required."),
    FIELD_EMAIL_REQUIRED("The email field is required."),
    FIELD_EMAIL_NOT_VALID("The email address is invalid."),
    FIELD_PAYMENT_OUT_RANGE("The base salary must be between 0 and 1,500,000."),
    USER_WITH_EMAIL_EXIST("There is already a user with the email address %s registered."),
    ROLE_NOT_FOUND("The role %s does not exist."),
    CREDENTIALS_NOT_FOUND("Credentials not found."),
    DO_NOT_ACCESS_RESOURCE("Doesn't have access to this resource."),
    UNAUTHORIZED_SENT_TOKEN_INVALID("Sent token is invalid."),
    EXPIRED_TOKEN("Expired token!");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
