package home.prozetx.lernenwor.controller;

import home.prozetx.lernenwor.domain.user.UserCreation;
import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.repository.UserRepository;
import home.prozetx.lernenwor.service.UserService;
import home.prozetx.lernenwor.service.mapper.UserMapper;
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
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public String get() {
        //User user = new User("name1");
        //userRepository.save(user);


        return Long.toString(userRepository.count());
        //return "get";
    }
    @PostMapping
    public ResponseEntity<?> createNew(@RequestBody @Valid UserCreation userCreation , BindingResult bindingResult) {
        //userRepository.save(userCreation);
        Map<String, Object> result = new HashMap<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .filter(fieldError -> fieldError.getDefaultMessage() != null)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            result.put("errors", errors);
            return ResponseEntity.badRequest().body(result);
        }
        User user = UserMapper.INSTANCE.userCreationToUser(userCreation);
        userRepository.save(user);
        return ResponseEntity.ok(result);
    }
}
