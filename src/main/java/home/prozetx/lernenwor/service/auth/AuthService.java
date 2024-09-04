package home.prozetx.lernenwor.service.auth;

import home.prozetx.lernenwor.domain.auth.RefreshToken;
import home.prozetx.lernenwor.domain.auth.SignIn;
import home.prozetx.lernenwor.domain.auth.SignUp;
import home.prozetx.lernenwor.domain.user.Role;
import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.exception.exceptions.*;
import home.prozetx.lernenwor.repository.UserRepository;
import home.prozetx.lernenwor.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String AUTH_HEADER_PREFIX = "Bearer ";

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private JwtService jwtService;

    public Map<String, String> emitNewAccessAndRefreshTokens(SignIn signIn) {
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

    public Map<String, String> emitNewAccessAndRefreshTokens(String refreshToken) {
        Claims claims;
        try {
            claims = jwtService.extractAllClaims(refreshToken);
        } catch (ExpiredJwtException ex) {
            throw new RefreshTokenExpiredException();
        }

        String username = jwtService.getUsername(claims);
        User user = userService.getUserByName(username);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", jwtService.generateAccessToken(user).getToken());
        tokens.put("refreshToken", jwtService.generateRefreshToken(user).getToken());
        return tokens;
    }

    private User getUserBySignIn(SignIn signIn) {
        Optional<User> foundedUser = userRepository.findByName(signIn.getName());
        if (foundedUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        return foundedUser.get();
    }

    public void setExpiredRefreshTokenToResponse(HttpServletResponse response) {
        RefreshToken refreshToken = jwtService.generateExpiredEmptyRefreshToken();
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken.getToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/api/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
    }

    public void addRefreshTokenToResponseAsCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/api/");
        response.addCookie(refreshTokenCookie);
    }

    public String extractRefreshTokenFromRequest(HttpServletRequest request) {
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken == null) {
            throw new RefreshTokenNotFoundException();
        }
        return refreshToken;
    }
}
