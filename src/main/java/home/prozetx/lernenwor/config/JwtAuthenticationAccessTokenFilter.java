package home.prozetx.lernenwor.config;

import home.prozetx.lernenwor.domain.user.User;
import home.prozetx.lernenwor.exception.exceptions.RefreshTokenExpiredException;
import home.prozetx.lernenwor.service.UserService;
import home.prozetx.lernenwor.service.auth.AuthService;
import home.prozetx.lernenwor.service.auth.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.util.Map;

import static home.prozetx.lernenwor.service.auth.AuthService.AUTH_HEADER_NAME;
import static home.prozetx.lernenwor.service.auth.AuthService.AUTH_HEADER_PREFIX;

@Component
@AllArgsConstructor
public class JwtAuthenticationAccessTokenFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private AuthService authService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTH_HEADER_NAME);
        if (authHeader == null || !authHeader.startsWith(AUTH_HEADER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authHeader.substring(AUTH_HEADER_PREFIX.length());
        try {
            Claims claims = jwtService.extractAllClaims(accessToken);
            if (!jwtService.isTokenExpired(claims)) {
                authService.authenticateUser(claims);
            } else {
                throw new ExpiredJwtException(null, claims,"Access token expired");
            }
        } catch (ExpiredJwtException ex) {
            String refreshToken = authService.extractRefreshTokenFromRequest(request);
            try {
                Claims claims = jwtService.extractAllClaims(refreshToken);
                if (!jwtService.isTokenExpired(claims)) {
                    Map<String, ?> tokens = authService.emitNewAccessAndRefreshTokens(refreshToken);
                    authService.addRefreshTokenToResponseAsCookie(response, tokens.get("refreshToken").toString());
                    response.setHeader(AUTH_HEADER_NAME, AUTH_HEADER_PREFIX + tokens.get("accessToken"));
                } else {
                    throw new ExpiredJwtException(null, claims,"Refresh token expired");
                }
            } catch (ExpiredJwtException e) {
                throw new RefreshTokenExpiredException();
            }
        }

        filterChain.doFilter(request, response);
    }
}
