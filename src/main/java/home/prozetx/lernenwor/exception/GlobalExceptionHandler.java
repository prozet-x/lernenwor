package home.prozetx.lernenwor.exception;

import home.prozetx.lernenwor.exception.exceptions.UserNameExists;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;

import static org.springframework.http.HttpStatus.CONFLICT;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNameExists.class)
    ResponseEntity<Map<String, Object>> handleUserNameExists(UserNameExists ex) {
        return ResponseEntity.status(CONFLICT).body(Map.of("error", ex.getMessage()));
    }
}
