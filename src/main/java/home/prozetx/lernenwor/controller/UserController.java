package home.prozetx.lernenwor.controller;

import home.prozetx.lernenwor.domain.user.UserCreation;
import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.repository.UserConfirmTokenRepository;
import home.prozetx.lernenwor.repository.UserRepository;
import home.prozetx.lernenwor.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserConfirmTokenRepository userConfirmTokenRepository;

    @GetMapping
    public ResponseEntity<?> get() {
        var result = new HashMap<String, Object>();
        result.put("users", userRepository.findAll());
//        var users = userRepository.findAll();
        result.put("tokens", userConfirmTokenRepository.findAll());

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> createNew(@RequestBody @Valid UserCreation userCreation , BindingResult bindingResult) {
        Map<String, Object> result = new HashMap<>();
        log.info("Attempt to register a new user: " + userCreation);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .filter(fieldError -> fieldError.getDefaultMessage() != null)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (existingValue, newValue) -> existingValue));
            result.put("errors", errors);
            log.info("The attempt failed: " + errors);
            return ResponseEntity.badRequest().body(result);
        }
        userService.saveUser(userCreation);
        return ResponseEntity.ok(result);
    }
}
