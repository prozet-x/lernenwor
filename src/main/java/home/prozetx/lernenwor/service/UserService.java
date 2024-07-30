package home.prozetx.lernenwor.service;

import home.prozetx.lernenwor.domain.auth.SignUp;
import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.domain.userConfirmToken.EmailConfirmToken;
import home.prozetx.lernenwor.exception.exceptions.UserEmailExists;
import home.prozetx.lernenwor.exception.exceptions.UserNameExists;
import home.prozetx.lernenwor.repository.EmailConfirmTokenRepository;
import home.prozetx.lernenwor.repository.UserRepository;
import home.prozetx.lernenwor.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailConfirmTokenRepository emailConfirmTokenRepository;

    @Transactional
    public User saveUser(SignUp signUp) {
        if (userRepository.existsByName(signUp.name())) {
            log.info("Attempt to create a user with an existing name: " + signUp);
            throw new UserNameExists(signUp.name());
        }
        if (userRepository.existsByEmail(signUp.email())) {
            log.info("Attempt to create a user with an existing email: " + signUp);
            throw new UserEmailExists(signUp.email());
        }
        User user = UserMapper.INSTANCE.signUpToUser(signUp);

        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("The new user " + user + " has been successfully saved");

        var userConfirmToken = new EmailConfirmToken(user);
        emailConfirmTokenRepository.save(userConfirmToken);
        log.info("The new user confirm token has been successfully saved. Token " + userConfirmToken);

        return savedUser;
    }

    public Boolean userExistsByUsername(String username) {
        return userRepository.existsByName(username);
    }

    public Boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email.toLowerCase());
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User with id %d does not exist", id));
            //NEED FIX. Need special exception
        }
        return user.get();
    }
}
