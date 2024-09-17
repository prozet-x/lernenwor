package home.prozetx.lernenwor.config;

import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.exception.exceptions.RefreshTokenNotFoundException;
import home.prozetx.lernenwor.service.auth.AuthService;
import home.prozetx.lernenwor.service.auth.FilterService;
import home.prozetx.lernenwor.service.auth.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@AllArgsConstructor
public class JwtAuthenticationRefreshTokenFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private FilterService filterService;
    //private AuthService authService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().equals("/api/v1/auth/updateTokens")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

//        String refreshToken = authService.extractRefreshTokenFromRequest(request);

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
        Claims claims = jwtService.extractAllClaims(refreshToken);
        System.out.println("Refresh. Claims extracted");
        if (jwtService.isTokenNotExpired(claims)) {
            filterService.authenticateUser(claims);
            System.out.println("Refresh. Authenticated.");
        }

        filterChain.doFilter(request, response);
    }
}
