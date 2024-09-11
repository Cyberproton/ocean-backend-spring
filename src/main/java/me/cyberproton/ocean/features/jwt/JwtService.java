package me.cyberproton.ocean.features.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nullable;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final JwtConfig jwtConfig;
    private final JwtParser jwtParser;

    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.jwtParser = Jwts.parser().verifyWith(getSecretKey()).build();
    }

    @Nullable public String getEmailFromToken(String token) {
        try {
            Jws<Claims> claims = parseToken(token).accept(Jws.CLAIMS);
            return claims.getPayload().getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public String generateToken(String email, Map<String, ?> extraClaims) {
        return buildToken(email, jwtConfig.accessTokenExpirationInMs(), extraClaims);
    }

    public String generateToken(String email) {
        return generateToken(email, Map.of());
    }

    public String generateRefreshToken(String email) {
        return buildToken(email, jwtConfig.refreshTokenExpirationInMs(), Map.of());
    }

    private String buildToken(String email, long expiration, Map<String, ?> extraClaims) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(email)
                .signWith(getSecretKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = getEmailFromToken(token);
            return username != null
                    && username.equals(userDetails.getUsername())
                    && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return parseToken(token).getPayload().getExpiration().before(new Date());
    }

    private Jws<Claims> parseToken(String token) {
        return jwtParser.parse(token).accept(Jws.CLAIMS);
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfig.secret().getBytes());
    }
}
