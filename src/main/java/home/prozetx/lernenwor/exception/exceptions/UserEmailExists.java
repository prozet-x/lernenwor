package home.prozetx.lernenwor.exception.exceptions;

public class UserEmailExists extends RuntimeException {
    public UserEmailExists(String userEmail) {
        super("User with email " + userEmail + " is already exists");
    }
}
