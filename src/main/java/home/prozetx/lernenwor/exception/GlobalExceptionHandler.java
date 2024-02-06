package home.prozetx.lernenwor.exception;

import home.prozetx.lernenwor.exception.exceptions.EmailTokenExists;
import home.prozetx.lernenwor.exception.exceptions.UserEmailExists;
import home.prozetx.lernenwor.exception.exceptions.UserNameExists;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNameExists.class)
    ResponseEntity<Map<String, Object>> handleUserNameExists(UserNameExists ex) {
        return ResponseEntity.status(CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(UserEmailExists.class)
    ResponseEntity<Map<String, Object>> handleUserEmailExists(UserEmailExists ex) {
        return ResponseEntity.status(CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(EmailTokenExists.class)
    ResponseEntity<Map<String, Object>> handleEmailTokenExists(EmailTokenExists ex) {
        return ResponseEntity.status(BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }
}
