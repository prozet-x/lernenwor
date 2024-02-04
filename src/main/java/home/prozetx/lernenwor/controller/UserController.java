package home.prozetx.lernenwor.controller;

import home.prozetx.lernenwor.domain.user.UserCreation;
import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.repository.UserRepository;
import home.prozetx.lernenwor.service.UserService;
import home.prozetx.lernenwor.service.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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

    @GetMapping
    public ResponseEntity<?> get() {
        //User user = new User("name1");
        //userRepository.save(user);

        var passwords = userRepository.findAll().stream()
                .map(User::getPassword)
                .toList();
        return ResponseEntity.ok(passwords);
        //return "get";
    }
    @PostMapping
    public ResponseEntity<?> createNew(@RequestBody @Valid UserCreation userCreation , BindingResult bindingResult) {
        Map<String, Object> result = new HashMap<>();
        log.info("Attempt to register a new user: " + userCreation.toSafeString());
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
