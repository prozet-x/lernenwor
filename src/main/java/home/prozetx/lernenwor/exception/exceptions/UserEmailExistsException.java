package home.prozetx.lernenwor.exception.exceptions;

public class UserEmailExistsException extends RuntimeException {
    public UserEmailExistsException(String userEmail) {
        super("User with email " + userEmail + " already exists");
    }
}
