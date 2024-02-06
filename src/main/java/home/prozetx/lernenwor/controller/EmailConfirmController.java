package home.prozetx.lernenwor.controller;

import home.prozetx.lernenwor.domain.userConfirmToken.EmailConfirmToken;
import home.prozetx.lernenwor.repository.EmailConfirmTokenRepository;
import home.prozetx.lernenwor.service.EmailConfirmService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/emailconfirm")
@AllArgsConstructor
public class EmailConfirmController {
    private final EmailConfirmService emailConfirmService;
    @GetMapping
    public ResponseEntity<?> confirmEmail(@RequestParam("token") String token) {
        emailConfirmService.confirmEmail(token);
        return ResponseEntity.ok().build();
    }
}
