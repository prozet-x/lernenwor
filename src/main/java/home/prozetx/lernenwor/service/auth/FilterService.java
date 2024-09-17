package home.prozetx.lernenwor.service.auth;

import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FilterService {
    private JwtService jwtService;
    private UserService userService;
    public void authenticateUser(Claims claims) {
        String username = jwtService.getUsername(claims);
        User user = userService.getUserByName(username);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authToken);
        SecurityContextHolder.setContext(securityContext);
    }
}
