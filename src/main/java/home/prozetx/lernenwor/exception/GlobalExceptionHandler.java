package home.prozetx.lernenwor.exception;

import home.prozetx.lernenwor.exception.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNameExistsException.class)
    ResponseEntity<Map<String, Object>> handleUserNameExists(UserNameExistsException ex) {
        return ResponseEntity.status(CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(UserEmailExistsException.class)
    ResponseEntity<Map<String, Object>> handleUserEmailExists(UserEmailExistsException ex) {
        return ResponseEntity.status(CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(EmailTokenNotFoundException.class)
    ResponseEntity<Map<String, Object>> handleEmailTokenExists(EmailTokenNotFoundException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<Map<String, Object>> handle(UserNotFoundException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    ResponseEntity<Map<String, Object>> handle(RefreshTokenNotFoundException ex) {
        return ResponseEntity.status(UNAUTHORIZED).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    ResponseEntity<Map<String, Object>> handle(RefreshTokenExpiredException ex) {
        return ResponseEntity.status(UNAUTHORIZED).body(Map.of("error", ex.getMessage()));
    }
//
//    @ExceptionHandler(AccessTokenExpiredException.class)
//    ResponseEntity<Map<String, Object>> handle(AccessTokenExpiredException ex) {
//        return ResponseEntity.status(UNAUTHORIZED).body(Map.of("error", ex.getMessage()));
//    }
}
