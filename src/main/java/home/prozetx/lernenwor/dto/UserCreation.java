package home.prozetx.lernenwor.dto;

public record UserCreation(
        String name,
        String email,
        String emailConfirm,
        String password,
        String passwordConfirm
) {
}
