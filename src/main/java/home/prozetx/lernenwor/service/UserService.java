package home.prozetx.lernenwor.service;

import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.domain.user.UserCreation;
import home.prozetx.lernenwor.domain.userConfirmToken.UserConfirmToken;
import home.prozetx.lernenwor.exception.exceptions.UserEmailExists;
import home.prozetx.lernenwor.exception.exceptions.UserNameExists;
import home.prozetx.lernenwor.repository.UserConfirmTokenRepository;
import home.prozetx.lernenwor.repository.UserRepository;
import home.prozetx.lernenwor.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserConfirmTokenRepository userConfirmTokenRepository;

    @Transactional
    public void saveUser(UserCreation userCreation) {
        if (userRepository.existsByName(userCreation.name())) {
            log.info("Attempt to create a user with an existing name: " + userCreation);
            throw new UserNameExists(userCreation.name());
        }
        if (userRepository.existsByEmail(userCreation.email())) {
            log.info("Attempt to create a user with an existing email: " + userCreation);
            throw new UserEmailExists(userCreation.email());
        }
        User user = UserMapper.INSTANCE.userCreationToUser(userCreation);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("The new user " + user + " has been successfully saved");

        var userConfirmToken = new UserConfirmToken(user);
        userConfirmTokenRepository.save(userConfirmToken);
        log.info("The new user confirm token " + userConfirmToken.toString() + " has been successfully saved");
    }
}
