package home.prozetx.lernenwor.service.auth;

import home.prozetx.lernenwor.domain.auth.AccessToken;
import home.prozetx.lernenwor.domain.auth.SignUp;
import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private UserService userService;
    private JwtService jwtService;
    public AccessToken signUp(SignUp signUp) {
        User user = userService.saveUser(signUp);
        return jwtService.generateToken(user);
    }

}
