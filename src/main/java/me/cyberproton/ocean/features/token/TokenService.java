package me.cyberproton.ocean.features.token;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.cyberproton.ocean.features.user.UserEntity;
import me.cyberproton.ocean.features.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final SecureRandom secureRandom = new SecureRandom();
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final TokenConfig tokenConfig;

    public TokenResult createToken(UserEntity user, TokenType tokenType) {
        String rawToken = generateToken();
        String hashedToken = passwordEncoder.encode(rawToken);
        Token token = Token.builder().user(user).type(tokenType).token(hashedToken).build();
        tokenRepository.save(token);
        return TokenResult.builder().token(token).rawToken(rawToken).build();
    }

    public TokenResult createEmailVerificationToken(UserEntity user, String email) {
        String rawToken = generateToken();
        String hashedToken = passwordEncoder.encode(rawToken);
        Token token =
                Token.builder()
                        .user(user)
                        .type(TokenType.EMAIL_VERIFICATION)
                        .token(hashedToken)
                        .email(email)
                        .build();
        tokenRepository.save(token);
        return TokenResult.builder().token(token).rawToken(rawToken).build();
    }

    public Optional<Token> findLatestToken(UserEntity user, TokenType tokenType) {
        return tokenRepository.findFirstByUserAndTypeOrderByCreatedAtDesc(user, tokenType);
    }

    public boolean checkToken(UserEntity user, String rawToken, TokenType tokenType) {
        Token res =
                tokenRepository
                        .findFirstByUserAndTypeOrderByCreatedAtDesc(user, tokenType)
                        .orElseThrow();
        // Check if token is expired
        if (res.getCreatedAt()
                .toInstant()
                .plusMillis(tokenConfig.passwordReset().maxAgeInMilliseconds())
                .isBefore(Instant.now())) {
            return false;
        }
        return passwordEncoder.matches(rawToken, res.getToken());
    }

    public Token validateToken(UserEntity user, String rawToken, TokenType tokenType) {
        Token res =
                tokenRepository
                        .findFirstByUserAndTypeOrderByCreatedAtDesc(user, tokenType)
                        .orElseThrow();
        // Check if token is expired
        if (res.getCreatedAt()
                .toInstant()
                .plusMillis(tokenConfig.passwordReset().maxAgeInMilliseconds())
                .isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
        }
        if (!passwordEncoder.matches(rawToken, res.getToken())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
        }
        return res;
    }

    private String generateToken() {
        return String.format("%06d", secureRandom.nextInt(999999));
    }
}
