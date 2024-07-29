package home.prozetx.lernenwor.controller;

import home.prozetx.lernenwor.domain.auth.AuthToken;
import home.prozetx.lernenwor.domain.auth.SignUp;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    @PostMapping("/signup")
    public ResponseEntity<AuthToken> signUp(@RequestBody @Valid SignUp) {

    }
}
