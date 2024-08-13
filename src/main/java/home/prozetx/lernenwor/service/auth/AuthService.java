package home.prozetx.lernenwor.service.auth;

import home.prozetx.lernenwor.domain.auth.AccessToken;
import home.prozetx.lernenwor.domain.auth.SignIn;
import home.prozetx.lernenwor.domain.auth.SignUp;
import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.exception.exceptions.UserNotFoundException;
import home.prozetx.lernenwor.repository.UserRepository;
import home.prozetx.lernenwor.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private UserService userService;
    private UserRepository userRepository;
    private JwtService jwtService;
    public AccessToken signIn(SignIn signIn) {
        Optional<User> user = userRepository.findByName(signIn.getName());
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        return jwtService.generateToken(user.get());
    }

}
