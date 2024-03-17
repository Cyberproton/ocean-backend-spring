package me.cyberproton.ocean.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private final JwtConfig jwtConfig;
    private final JwtParser jwtParser;

    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.jwtParser = Jwts.parser().verifyWith(getSecretKey()).build();
    }

    public long getUserIdFromToken(String token) {
        Jws<Claims> claims = parseToken(token).accept(Jws.CLAIMS);
        return Long.parseLong(claims.getPayload().getSubject());
    }

    public String generateToken(Long userId, Map<String, ?> extraClaims) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userId.toString())
                .signWith(getSecretKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.expirationInMilliseconds()))
                .compact();
    }

    public String generateToken(Long userId) {
        return generateToken(userId, Map.of());
    }

    public boolean isTokenValid(String token, AppUserDetails userDetails) {
        try {
            long userId = getUserIdFromToken(token);
            return userId == userDetails.getId() && !isTokenExpired(token);
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
