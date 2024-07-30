package home.prozetx.lernenwor.controller;

import home.prozetx.lernenwor.repository.EmailConfirmTokenRepository;
import home.prozetx.lernenwor.repository.UserRepository;
import home.prozetx.lernenwor.service.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("v1/users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailConfirmTokenRepository emailConfirmTokenRepository;


    @GetMapping
    public ResponseEntity<?> get() {
        var result = new HashMap<String, Object>();
        result.put("users", userRepository.findAll());
        result.put("tokens", emailConfirmTokenRepository.findAll());

        return ResponseEntity.ok(result);
    }

//    @PostMapping
//    public ResponseEntity<?> createNew(@RequestBody @Valid UserCreation userCreation , BindingResult bindingResult) {
//        Map<String, Object> result = new HashMap<>();
//        log.info(String.format("Attempt to register a new user: %s", userCreation));
//        if (bindingResult.hasErrors()) {
//            Map<String, String> errors = bindingResult.getFieldErrors().stream()
//                    .filter(fieldError -> fieldError.getDefaultMessage() != null)
//                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (existingValue, newValue) -> existingValue));
//            result.put("errors", errors);
//            log.info("The attempt failed: " + errors);
//            return ResponseEntity.badRequest().body(result);
//        }
//
//        User user = userService.saveUser(userCreation);
//
//        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/{id}")
//                .buildAndExpand(user.getId())
//                .toUri();
//
//        return ResponseEntity.created(location).body(user);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserData(@PathVariable("id") @NotBlank Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("user", userService.getUserById(id));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/checks/check-user-name-exists/{username}")
    public ResponseEntity<?> checkUserNameExists(@PathVariable("username") @NotBlank String username) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("username_exists", userService.userExistsByUsername(username));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/checks/check-user-email-exists/{email}")
    public ResponseEntity<?> checkUserEmailExists(@PathVariable("email") @NotBlank String email) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("user_email_exists", userService.userExistsByEmail(email));
        return ResponseEntity.ok(result);
    }
}
