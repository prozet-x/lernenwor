package home.prozetx.lernenwor.config;

import home.prozetx.lernenwor.domain.user.User;
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
    private FilterUtils filterUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Refresh filter. Entered");
        if (!request.getRequestURI().equals("/api/v1/auth/updateTokens")) {
            System.out.println("Refresh filter. Not need to refresh");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Refresh filter. Ready to check auth condition");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("Refresh filter. Authenticated");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Refresh filter. Ready to extract token from the cookie");
        Cookie[] cookies = request.getCookies();
        String refreshToken =
        Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .map(cookie -> cookie.getValue())
                .orElse(null);

        if (refreshToken == null) {
            System.out.println("Refresh filter. Not found refresh token");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Refresh filter. Ready to get claims from the token");
        Claims claims = jwtService.extractAllClaims(refreshToken);
        if (!jwtService.isTokenExpired(claims)) {
            System.out.println("Refresh filter. Token didn't expire");
            filterUtils.authenticateUser(claims);
        }

        System.out.println("Refresh filter. End");
        filterChain.doFilter(request, response);
    }
}
