package me.cyberproton.ocean.features.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Nullable
    public String getUsernameFromToken(String token) {
        try {
            Jws<Claims> claims = parseToken(token).accept(Jws.CLAIMS);
            return claims.getPayload().getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public String generateToken(String username, Map<String, ?> extraClaims) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .signWith(getSecretKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.expirationInMilliseconds()))
                .compact();
    }

    public String generateToken(String username) {
        return generateToken(username, Map.of());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = getUsernameFromToken(token);
            return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
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
