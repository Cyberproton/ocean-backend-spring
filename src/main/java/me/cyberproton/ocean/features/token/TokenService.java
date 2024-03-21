package me.cyberproton.ocean.features.token;

import lombok.RequiredArgsConstructor;
import me.cyberproton.ocean.features.user.User;
import me.cyberproton.ocean.features.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final SecureRandom secureRandom = new SecureRandom();
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final TokenConfig tokenConfig;

    public Token createToken(User user, TokenType tokenType) {
        String rawToken = generateToken();
        System.out.println("rawToken: " + rawToken);
        String hashedToken = passwordEncoder.encode(rawToken);
        Token token = Token.builder()
                .user(user)
                .type(tokenType)
                .token(hashedToken)
                .build();
        return tokenRepository.save(token);
    }

    public Optional<Token> findLatestToken(User user, TokenType tokenType) {
        return tokenRepository.findFirstByUserAndTypeOrderByCreatedAtDesc(user, tokenType);
    }

    public boolean validateToken(User user, String rawToken, TokenType tokenType) {
        Token res = tokenRepository.findFirstByUserAndTypeOrderByCreatedAtDesc(user, tokenType).orElseThrow();
        // Check if token is expired
        if (res.getCreatedAt().toInstant().plusMillis(tokenConfig.resetPassword().maxAgeInMilliseconds()).isBefore(Instant.now())) {
            return false;
        }
        return passwordEncoder.matches(rawToken, res.getToken());
    }

    private String generateToken() {
        return String.format("%06d", secureRandom.nextInt(999999));
    }
}
