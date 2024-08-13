package home.prozetx.lernenwor.exception;

import home.prozetx.lernenwor.exception.exceptions.EmailTokenNotFoundException;
import home.prozetx.lernenwor.exception.exceptions.UserEmailExistsException;
import home.prozetx.lernenwor.exception.exceptions.UserNameExistsException;
import home.prozetx.lernenwor.exception.exceptions.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

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
}
