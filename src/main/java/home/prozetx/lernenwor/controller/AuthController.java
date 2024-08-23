package home.prozetx.lernenwor.controller;

import home.prozetx.lernenwor.domain.auth.SignIn;
import home.prozetx.lernenwor.domain.auth.SignUp;
import home.prozetx.lernenwor.service.UserService;
import home.prozetx.lernenwor.service.auth.AuthService;
import home.prozetx.lernenwor.service.auth.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

@RestController
@RequestMapping("v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    AuthService authService;
    JwtService jwtService;
    UserService userService;
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

        userService.saveUser(signUp);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignIn signIn, HttpServletResponse response) {
        Map<String , ?> tokens = authService.getAccessAndRefreshTokens(signIn);

        Cookie refreshTokenCookie = new Cookie("refreshToken", tokens.get("refreshToken").toString());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/api/");
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok().body(Map.of("accessToken", tokens.get("accessToken")));
    }
}
