package home.prozetx.lernenwor.exception.exceptions;

public class UserNameExists extends RuntimeException {
    public UserNameExists(String userName) {
        super("User with name '" + userName + "' already exists");
    }
}
