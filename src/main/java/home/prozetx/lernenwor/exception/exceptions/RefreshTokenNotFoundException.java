package home.prozetx.lernenwor.exception.exceptions;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException() { super("Refresh token was not found in the request"); }
}
