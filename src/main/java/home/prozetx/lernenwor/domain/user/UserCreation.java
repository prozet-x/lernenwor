package home.prozetx.lernenwor.domain.user;

import home.prozetx.lernenwor.domain.user.constraint.Password;
import home.prozetx.lernenwor.domain.user.constraint.Username;
import home.prozetx.lernenwor.service.constraint.FieldsMatchConsiderCase;
import jakarta.validation.constraints.Email;

@FieldsMatchConsiderCase(firstField = "password", secondField = "passwordConfirm", message = "Fields {firstField} and {secondField} must be equal")
public record UserCreation(
        @Username
        String name,
        @Email
        String email,
        @Password
        String password,
        String passwordConfirm
) {
        public String toSafeString() {
                return "UserCreation {" +
                        "name=" + name +
                        ", email=" + email +
                        "}";
        }
}
