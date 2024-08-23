package home.prozetx.lernenwor.service.auth;

import home.prozetx.lernenwor.domain.auth.SignIn;
import home.prozetx.lernenwor.domain.auth.SignUp;
import home.prozetx.lernenwor.domain.user.Role;
import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.domain.userConfirmToken.EmailConfirmToken;
import home.prozetx.lernenwor.exception.exceptions.UserEmailExistsException;
import home.prozetx.lernenwor.exception.exceptions.UserNameExistsException;
import home.prozetx.lernenwor.exception.exceptions.UserNotFoundException;
import home.prozetx.lernenwor.repository.UserRepository;
import home.prozetx.lernenwor.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private UserService userService;
    PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private JwtService jwtService;

    public Map<String, String> getAccessAndRefreshTokens(SignIn signIn) {
        User user = getUserBySignIn(signIn);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", jwtService.generateAccessToken(user).getToken());
        tokens.put("refreshToken", jwtService.generateRefreshToken(user).getToken());
        return tokens;
    }

    public void signUp(SignUp signUp) {
        log.info("Attempt to sign up a user: " + signUp);
        if (userRepository.existsByName(signUp.name())) {
            throw new UserNameExistsException(signUp.name());
        }
        if (userRepository.existsByEmail(signUp.email())) {
            throw new UserEmailExistsException(signUp.email());
        }

        User user = User.builder()
                .name(signUp.name())
                .email(signUp.email().toLowerCase())
                .password(passwordEncoder.encode(signUp.password()))
                .confirmed(false)
                .role(Role.ROLE_USER)
                .build();

        userService.saveUser(user);
    }

    private User getUserBySignIn(SignIn signIn) {
        Optional<User> foundedUser = userRepository.findByName(signIn.getName());
        if (foundedUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        return foundedUser.get();
    }
}
