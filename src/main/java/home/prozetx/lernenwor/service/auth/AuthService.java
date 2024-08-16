package home.prozetx.lernenwor.service.auth;

import home.prozetx.lernenwor.domain.auth.AccessToken;
import home.prozetx.lernenwor.domain.auth.SignIn;
import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.exception.exceptions.UserNotFoundException;
import home.prozetx.lernenwor.repository.UserRepository;
import home.prozetx.lernenwor.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private UserService userService;
    private UserRepository userRepository;
    private JwtService jwtService;
    public Map<String, String> signIn(SignIn signIn) {
        Optional<User> foundedUser = userRepository.findByName(signIn.getName());
        if (foundedUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        User user = foundedUser.get();
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", jwtService.generateAccessToken(user).getToken());
        tokens.put("refreshToken", jwtService.generateRefreshToken(user).getToken());
        return tokens;
    }

}
