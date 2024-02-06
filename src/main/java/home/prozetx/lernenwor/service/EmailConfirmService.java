package home.prozetx.lernenwor.service;

import home.prozetx.lernenwor.domain.userConfirmToken.EmailConfirmToken;
import home.prozetx.lernenwor.exception.exceptions.EmailTokenExists;
import home.prozetx.lernenwor.repository.EmailConfirmTokenRepository;
import home.prozetx.lernenwor.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class EmailConfirmService {
    private final EmailConfirmTokenRepository emailConfirmTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public void confirmEmail(String token) {
        log.info("An attempt to confirm an email address using a token.");
        EmailConfirmToken emailConfirmToken = emailConfirmTokenRepository.findByToken(token);
        if (emailConfirmToken == null) {
            log.info("The attempt failed. No such token.");
            throw new EmailTokenExists("Wrong attempt to confirm email. Token does not exist.");
        }

        var user = emailConfirmToken.getUser();
        emailConfirmTokenRepository.deleteByUser(user);
        user.confirmEmail();
        userRepository.save(user);
        log.info("The email address " + user.getEmail() + " for the " + user.getName() + " user has been successfully confirmed.");
    }
}
