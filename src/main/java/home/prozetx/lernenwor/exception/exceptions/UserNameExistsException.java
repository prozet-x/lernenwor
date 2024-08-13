package home.prozetx.lernenwor.exception.exceptions;

public class UserNameExistsException extends RuntimeException {
    public UserNameExistsException(String userName) {
        super("User with name '" + userName + "' already exists");
    }
}
