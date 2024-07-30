package home.prozetx.lernenwor.controller;

import home.prozetx.lernenwor.domain.auth.AccessToken;
import home.prozetx.lernenwor.domain.auth.SignUp;
import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUp signUp, BindingResult bindingResult) {
        Map<String, Object> result = new HashMap<>();
        log.info(String.format("Attempt to register a new user: %s", signUp));
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .filter(fieldError -> fieldError.getDefaultMessage() != null)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (existingValue, newValue) -> existingValue));
            result.put("errors", errors);
            log.info("The attempt failed: " + errors);
            return ResponseEntity.badRequest().body(result);
        }

        AccessToken accessToken = authService.signUp(signUp);


//        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/{id}")
//                .buildAndExpand(user.getId())
//                .toUri();

        return ResponseEntity.ok(accessToken);
    }

    @GetMapping("/signup")
    public ResponseEntity<Boolean> test() {
        return ResponseEntity.ok(true);
    }
}
