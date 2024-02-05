package home.prozetx.lernenwor.controller;

import home.prozetx.lernenwor.domain.userConfirmToken.UserConfirmToken;
import home.prozetx.lernenwor.repository.UserConfirmTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emailconfirm")
@AllArgsConstructor
public class EmailConfirm {
    private final UserConfirmTokenRepository userConfirmTokenRepository;

    @GetMapping
    public ResponseEntity<?> confirmEmail(@RequestParam("token") String token) {
        UserConfirmToken userConfirmToken = userConfirmTokenRepository.findByToken(token);
        if (userConfirmToken != null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.noContent().build();
    }
}
