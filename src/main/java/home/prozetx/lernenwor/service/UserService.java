package home.prozetx.lernenwor.service;

import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.domain.user.UserCreation;
import home.prozetx.lernenwor.exception.exceptions.UserEmailExists;
import home.prozetx.lernenwor.exception.exceptions.UserNameExists;
import home.prozetx.lernenwor.repository.UserRepository;
import home.prozetx.lernenwor.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Optional<User> saveUser(UserCreation userCreation) {
        if (userRepository.existsByName(userCreation.name())) {
            log.info("Attempt to create a user with an existing name: " + userCreation);
            throw new UserNameExists(userCreation.name());
        }
        if (userRepository.existsByEmail(userCreation.email())) {
            log.info("Attempt to create a user with an existing email: " + userCreation);
            throw new UserEmailExists(userCreation.email());
        }
        User user = UserMapper.INSTANCE.userCreationToUser(userCreation);
        userRepository.save(user);
        log.info("The new user " + user + " has been successfully saved");
        return Optional.of(user);
    }
}
