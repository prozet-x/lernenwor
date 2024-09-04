package home.prozetx.lernenwor.controller;

import home.prozetx.lernenwor.domain.auth.SignIn;
import home.prozetx.lernenwor.domain.auth.SignUp;
import home.prozetx.lernenwor.exception.exceptions.RefreshTokenNotFoundException;
import home.prozetx.lernenwor.service.UserService;
import home.prozetx.lernenwor.service.auth.AuthService;
import home.prozetx.lernenwor.service.auth.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .filter(fieldError -> fieldError.getDefaultMessage() != null)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (existingValue, newValue) -> existingValue));
            result.put("errors", errors);
            log.info("The signup attempt failed: " + errors);
            return ResponseEntity.badRequest().body(result);
        }

        authService.signUp(signUp);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/signin")
    public ResponseEntity<Map<String, ?>> signIn(@RequestBody @Valid SignIn signIn, HttpServletResponse response) {
        Map<String , ?> tokens = authService.emitNewAccessAndRefreshTokens(signIn);
        authService.addRefreshTokenToResponseAsCookie(response, tokens.get("refreshToken").toString());
        return ResponseEntity.ok().body(Map.of("accessToken", tokens.get("accessToken")));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut(HttpServletResponse response) {
        authService.setExpiredRefreshTokenToResponse(response);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/updateTokens")
    public ResponseEntity<Map<String, ?>> updateTokens(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = authService.extractRefreshTokenFromRequest(request);
        Map<String, ?> tokens = authService.emitNewAccessAndRefreshTokens(refreshToken);
        authService.addRefreshTokenToResponseAsCookie(response, tokens.get("refreshToken").toString());
        return ResponseEntity.ok().body(Map.of("accessToken", tokens.get("accessToken")));
    }
}
