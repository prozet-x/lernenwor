package home.prozetx.lernenwor.domain.user;

public record UserCreation(
        String name,
        String email,
        String emailConfirm,
        String password,
        String passwordConfirm
) {
}
