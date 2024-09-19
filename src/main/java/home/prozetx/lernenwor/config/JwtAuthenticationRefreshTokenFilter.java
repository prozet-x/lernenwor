package home.prozetx.lernenwor.config;

import home.prozetx.lernenwor.service.auth.FilterService;
import home.prozetx.lernenwor.service.auth.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import static home.prozetx.lernenwor.config.SecurityConfig.UPDATE_TOKENS_ENDPOINT;

@Component
@AllArgsConstructor
public class JwtAuthenticationRefreshTokenFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private FilterService filterService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getRequestURI().equals(UPDATE_TOKENS_ENDPOINT);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().equals(UPDATE_TOKENS_ENDPOINT)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Refresh. Try to extract claims");
        Claims claims;
        try {
            claims = jwtService.extractAllClaims(refreshToken);
            filterService.authenticateUser(claims);
            System.out.println("Refresh. Authenticated.");
        } catch (ExpiredJwtException ex) {
            System.out.println("Refresh. Token expired.");
        }

        filterChain.doFilter(request, response);
    }
}
