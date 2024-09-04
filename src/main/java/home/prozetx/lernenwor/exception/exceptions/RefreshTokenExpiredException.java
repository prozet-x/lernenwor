package home.prozetx.lernenwor.exception.exceptions;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException() { super("Refresh token has been expired"); }
}
