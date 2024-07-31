package home.prozetx.lernenwor.service.auth;

import home.prozetx.lernenwor.domain.auth.AccessToken;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${token.key}")
    private String signKey;
    private static final int accessTokenLifeTimeSeconds = 1800;

    public AccessToken generateToken(UserDetails user) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("name", userDetails.getUsername());

        String token = Jwts.builder()
//                .claims(claims)
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() +  + accessTokenLifeTimeSeconds))
                        .subject(user.getUsername())
                        //.signWith(Keys.hmacShaKeyFor(signKey.getBytes()), Jwts.SIG.HS256)
                        .signWith(Keys.hmacShaKeyFor(signKey.getBytes()))
                        .compact();
        return new AccessToken(token);
    }
}
