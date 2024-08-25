package home.prozetx.lernenwor.config;

import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.service.UserService;
import home.prozetx.lernenwor.service.auth.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String AUTH_HEADER_PREFIX = "Bearer ";
    private JwtService jwtService;
    private UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTH_HEADER_NAME);
        if (authHeader == null || !authHeader.startsWith(AUTH_HEADER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(AUTH_HEADER_PREFIX.length());
        Claims claims = jwtService.extractAllClaims(token);
        System.out.println(claims.toString());
        if (!jwtService.isTokenExpired(claims)) {
            System.out.println("Token did not expire");
            String username = jwtService.getUsername(claims);
            User user = userService.getUserByName(username);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authToken);
            SecurityContextHolder.setContext(securityContext);
        }

        filterChain.doFilter(request, response);
    }
}
