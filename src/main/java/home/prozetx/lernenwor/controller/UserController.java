package home.prozetx.lernenwor.controller;

import home.prozetx.lernenwor.domain.user.UserCreation;
import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@RequestMapping("users")
public class UserController {
    private final UserRepository userRepository;
    @GetMapping
    public String get() {
        User user = new User("name1");
        userRepository.save(user);


        return Long.toString(userRepository.count());
    }
    @PostMapping
    public ResponseEntity<?> createNew(@RequestBody @Valid UserCreation userCreation , BindingResult bindingResult) {
        //userRepository.save(user);
        Map<String, Object> result = new HashMap<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .filter(fieldError -> fieldError.getDefaultMessage() != null)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            result.put("errors", errors);
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }
}
