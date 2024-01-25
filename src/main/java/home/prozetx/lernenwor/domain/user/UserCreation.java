package home.prozetx.lernenwor.domain.user;

import home.prozetx.lernenwor.domain.user.constraint.Password;
import home.prozetx.lernenwor.domain.user.constraint.Username;
import home.prozetx.lernenwor.service.constraint.FieldsMatchConsiderCase;
import home.prozetx.lernenwor.service.constraint.FieldsMatchIgnoreCase;
import jakarta.validation.constraints.Email;

@FieldsMatchConsiderCase(firstField = "password", secondField = "passwordConfirm", message = "Fields {firstField} and {secondField} must be equal")
@FieldsMatchIgnoreCase(firstField = "email", secondField = "emailConfirm", message = "Fields {firstField} and {secondField} must have the same email")
public record UserCreation(
        @Username
        String name,
        @Email
        String email,
        String emailConfirm,
        @Password
        String password,
        String passwordConfirm
) {
}
