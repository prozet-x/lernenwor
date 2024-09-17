package home.prozetx.lernenwor.exception.exceptions;

public class AccessTokenExpiredException extends RuntimeException {
    public AccessTokenExpiredException() { super("Access token has expired"); }
}
