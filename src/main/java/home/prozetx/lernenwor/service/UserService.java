package home.prozetx.lernenwor.service;

import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.domain.EmailConfirmToken.EmailConfirmToken;
import home.prozetx.lernenwor.repository.EmailConfirmTokenRepository;
import home.prozetx.lernenwor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final EmailConfirmTokenRepository emailConfirmTokenRepository;

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
        log.info("The new user " + user + " has been successfully saved");

        var userConfirmToken = new EmailConfirmToken(user);
        emailConfirmTokenRepository.save(userConfirmToken);
        log.info("The new user confirm token has been successfully saved. Token " + userConfirmToken);
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

    public User getUserByName(String name) {
        Optional<User> user = userRepository.findByName(name);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User with name %s does not exist", name));
            //NEED FIX. Need special exception
        }
        return user.get();
    }
}
