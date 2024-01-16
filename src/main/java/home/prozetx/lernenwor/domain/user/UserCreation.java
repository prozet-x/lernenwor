package home.prozetx.lernenwor.domain.user;

import home.prozetx.lernenwor.domain.user.constraint.Password;
import home.prozetx.lernenwor.domain.user.constraint.Username;

public record UserCreation(
        @Username
        String name,
        String email,
        String emailConfirm,
        @Password
        String password,
        String passwordConfirm
) {
}
