package home.prozetx.lernenwor.config;

import home.prozetx.lernenwor.service.auth.FilterService;
import home.prozetx.lernenwor.service.auth.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

import static home.prozetx.lernenwor.config.SecurityConfig.AUTH_PATTERN;
import static home.prozetx.lernenwor.config.SecurityConfig.UPDATE_TOKENS_ENDPOINT;
import static home.prozetx.lernenwor.config.SecurityConfig.ALL_USERS_ENDPOINT;
import static home.prozetx.lernenwor.config.SecurityConfig.CHECK_EXISTING_USER_DATA_PATTERN;
import static home.prozetx.lernenwor.service.auth.AuthService.AUTH_HEADER_NAME;
import static home.prozetx.lernenwor.service.auth.AuthService.AUTH_HEADER_PREFIX;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationAccessTokenFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final FilterService filterService;
    @Value("${spring.mvc.servlet.path}")
    private String APIPath;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();

        String method = request.getMethod();
        return (requestURI.startsWith(APIPath + AUTH_PATTERN)  && method.equals(HttpMethod.POST.toString()))
                || (requestURI.equals(APIPath + UPDATE_TOKENS_ENDPOINT) && method.equals(HttpMethod.GET.toString()))
                || (requestURI.equals(APIPath + ALL_USERS_ENDPOINT) && method.equals(HttpMethod.GET.toString()))
                || (requestURI.startsWith(APIPath + CHECK_EXISTING_USER_DATA_PATTERN) && method.equals(HttpMethod.GET.toString()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTH_HEADER_NAME);
        if (authHeader == null || !authHeader.startsWith(AUTH_HEADER_PREFIX)) {
            System.out.println("Access. No header");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing or invalid");
            return;
        }

        String accessToken = authHeader.substring(AUTH_HEADER_PREFIX.length());
        try {
            System.out.println("Access. Try to extract claims");
            Claims claims = jwtService.extractAllClaims(accessToken);
            System.out.println("Access. Claims extracted");
            filterService.authenticateUser(claims);
            System.out.println("Access. Authenticated");
        } catch (ExpiredJwtException ex) {
            System.out.println("Access. Token expired with exception.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token has expired");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
