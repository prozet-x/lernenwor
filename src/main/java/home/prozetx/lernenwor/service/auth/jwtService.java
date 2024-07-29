package home.prozetx.lernenwor.service.auth;

import home.prozetx.lernenwor.config.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class jwtService {
    @Value("${token.key}")
    private String signKey;
    private static final int accessTokenLifeTimeSeconds = 1800;

    public String generateToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("name", userDetails.getUsername());

        return Jwts.builder()
//                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() +  + accessTokenLifeTimeSeconds))
                .subject(userDetails.getUsername())
                .signWith(Keys.hmacShaKeyFor(signKey.getBytes()))
                .compact();
    }
}
