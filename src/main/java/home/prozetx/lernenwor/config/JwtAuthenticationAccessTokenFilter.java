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

import static home.prozetx.lernenwor.service.auth.AuthService.AUTH_HEADER_NAME;
import static home.prozetx.lernenwor.service.auth.AuthService.AUTH_HEADER_PREFIX;

@Component
@AllArgsConstructor
public class JwtAuthenticationAccessTokenFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private UserService userService;
    private FilterUtils filterUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Access filter. Entered");
        String authHeader = request.getHeader(AUTH_HEADER_NAME);
        if (authHeader == null || !authHeader.startsWith(AUTH_HEADER_PREFIX)) {
            System.out.println("Access filter. Auth header not found");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(AUTH_HEADER_PREFIX.length());
        Claims claims = jwtService.extractAllClaims(token);
        if (!jwtService.isTokenExpired(claims)) {
            System.out.println("Access filter. Expired");
            filterUtils.authenticateUser(claims);
        }

        System.out.println("Access filter. All good");
        filterChain.doFilter(request, response);
    }
}
