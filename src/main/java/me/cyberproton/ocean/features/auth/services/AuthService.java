package me.cyberproton.ocean.features.auth.services;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.auth.dtos.*;
import me.cyberproton.ocean.features.jwt.JwtService;
import me.cyberproton.ocean.features.token.Token;
import me.cyberproton.ocean.features.token.TokenConfig;
import me.cyberproton.ocean.features.token.TokenService;
import me.cyberproton.ocean.features.token.TokenType;
import me.cyberproton.ocean.features.user.User;
import me.cyberproton.ocean.features.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final TokenConfig tokenConfig;

    public LoginResponse register(RegisterRequest registerRequest) {
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user.getUsername());
        return LoginResponse.builder().accessToken(token).build();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        var user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user.getUsername());
        return LoginResponse.builder().accessToken(token).build();
    }

    public ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest) {
        String usernameOrEmail = resetPasswordRequest.getUsernameOrEmail();
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow();
        Optional<Token> latestTokenOpt = tokenService.findLatestToken(user, TokenType.RESET_PASSWORD);
        latestTokenOpt.ifPresent((latestToken) -> {
            if (latestToken.getCreatedAt().toInstant().plusMillis(tokenConfig.resetPassword().intervalInMilliseconds()).isAfter(Instant.now())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Please wait for %s seconds before requesting another reset password".formatted(tokenConfig.resetPassword().intervalInMilliseconds() / 1000)
                );
            }
        });
        Token token = tokenService.createToken(user, TokenType.RESET_PASSWORD);
        return ResetPasswordResponse.builder().build();
    }

    public ConfirmResetPasswordResponse confirmResetPassword(ConfirmResetPasswordRequest confirmResetPasswordRequest) {
        User user = userRepository.findByUsernameOrEmail(confirmResetPasswordRequest.getUsernameOrEmail(), confirmResetPasswordRequest.getUsernameOrEmail()).orElseThrow();
        boolean matched = tokenService.validateToken(user, confirmResetPasswordRequest.getToken(), TokenType.RESET_PASSWORD);
        if (!matched) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid token"
            );
        }
        user.setPassword(passwordEncoder.encode(confirmResetPasswordRequest.getNewPassword()));
        userRepository.save(user);
        return ConfirmResetPasswordResponse.builder().message("Password reset successfully").build();
    }
}
