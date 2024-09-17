package home.prozetx.lernenwor.service.auth;

import home.prozetx.lernenwor.domain.auth.AccessToken;
import home.prozetx.lernenwor.domain.auth.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${token.key}")
    private String signKey;
    private static final int accessTokenLifeTimeSeconds = 30 * 1000; // 30 sec
    private static final int refreshTokenLifeTimeSeconds = 2 * 60 * 1000; // 2 min

    public AccessToken generateAccessToken(UserDetails user) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("name", userDetails.getUsername());

        String token = Jwts.builder()
//                .claims(claims)
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() +  + accessTokenLifeTimeSeconds))
                        .subject(user.getUsername())
                        .signWith(Keys.hmacShaKeyFor(signKey.getBytes()))
                        .compact();
        return new AccessToken(token);
    }

    public RefreshToken generateRefreshToken(UserDetails user) {
        String token = Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenLifeTimeSeconds))
                .subject(user.getUsername())
                .signWith(Keys.hmacShaKeyFor(signKey.getBytes()))
                .compact();
        return new RefreshToken(token);
    }

    public RefreshToken generateExpiredEmptyRefreshToken() {
        String token = Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() - refreshTokenLifeTimeSeconds))
                .signWith(Keys.hmacShaKeyFor(signKey.getBytes()))
                .compact();
        return new RefreshToken(token);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(signKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(Claims claims) {
        return claims.getSubject();
    }

    public boolean isTokenNotExpired(Claims claims) {
        return !claims.getExpiration().before(new Date());
    }
}
