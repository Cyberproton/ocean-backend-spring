package me.cyberproton.ocean.features.auth.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.auth.dtos.*;
import me.cyberproton.ocean.features.email.EmailService;
import me.cyberproton.ocean.features.email.EmailTemplateRequest;
import me.cyberproton.ocean.features.email.EmailTemplates;
import me.cyberproton.ocean.features.jwt.JwtService;
import me.cyberproton.ocean.features.role.RoleRepository;
import me.cyberproton.ocean.features.token.*;
import me.cyberproton.ocean.features.user.User;
import me.cyberproton.ocean.features.user.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final TokenConfig tokenConfig;
    private final EmailService emailService;
    @Qualifier(value = "emailMessageSource")
    private final MessageSource messageSource;

    @Transactional
    public LoginResponse register(RegisterRequest registerRequest) {
        User user = User.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
        roleRepository.findByName("USER").ifPresent(user::addRole);
        userRepository.save(user);
        emailService.sendEmailUsingTemplate(
                EmailTemplateRequest.builder()
                        .to(user.getEmail())
                        .subject(messageSource.getMessage("welcome.subject", null, LocaleContextHolder.getLocale()))
                        .template(EmailTemplates.WELCOME)
                        .model(Map.of(
                                "recipientName", Objects.requireNonNullElse(user.getUsername(), user.getEmail())
                        ))
                        .build()
        );
        String token = jwtService.generateToken(user.getUsername());
        return LoginResponse.builder().accessToken(token).build();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user.getEmail());
        return LoginResponse.builder().accessToken(token).build();
    }

    public ResetPasswordResponse requestResetPassword(ResetPasswordRequest resetPasswordRequest) {
        String usernameOrEmail = resetPasswordRequest.getUsernameOrEmail();
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow();
        Optional<Token> latestTokenOpt = tokenService.findLatestToken(user, TokenType.PASSWORD_RESET);
        latestTokenOpt.ifPresent((latestToken) -> {
            if (latestToken.getCreatedAt().toInstant().plusMillis(tokenConfig.passwordReset().intervalInMilliseconds()).isAfter(Instant.now())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Please wait for %s seconds before requesting another reset password".formatted(tokenConfig.passwordReset().intervalInMilliseconds() / 1000)
                );
            }
        });
        TokenResult token = tokenService.createToken(user, TokenType.PASSWORD_RESET);
        emailService.sendEmailUsingTemplate(EmailTemplateRequest.builder()
                .to(user.getEmail())
                .subject(messageSource.getMessage("password.reset.subject", null, LocaleContextHolder.getLocale()))
                .template(EmailTemplates.PASSWORD_RESET)
                .model(Map.of(
                        "recipientName", Objects.requireNonNullElse(user.getUsername(), user.getEmail()),
                        "token", token.rawToken(),
                        "expiryInMinutes", tokenConfig.passwordReset().maxAgeInMilliseconds() / 60000
                ))
                .build()
        );
        return ResetPasswordResponse.builder().build();
    }

    public ConfirmResetPasswordResponse confirmResetPassword(ConfirmResetPasswordRequest confirmResetPasswordRequest) {
        User user = userRepository.findByUsernameOrEmail(confirmResetPasswordRequest.getUsernameOrEmail(), confirmResetPasswordRequest.getUsernameOrEmail()).orElseThrow();
        tokenService.validateToken(user, confirmResetPasswordRequest.getToken(), TokenType.PASSWORD_RESET);
        user.setPassword(passwordEncoder.encode(confirmResetPasswordRequest.getNewPassword()));
        userRepository.save(user);
        return ConfirmResetPasswordResponse.builder().message("Password reset successfully").build();
    }

    public RequestVerifyEmailResponse requestVerifyEmail(User user) {
        Optional<Token> latestTokenOpt = tokenService.findLatestToken(user, TokenType.EMAIL_VERIFICATION);
        latestTokenOpt.ifPresent((latestToken) -> {
            if (latestToken.getCreatedAt().toInstant().plusMillis(tokenConfig.emailVerification().intervalInMilliseconds()).isAfter(Instant.now())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Please wait for %s seconds before requesting another verify email".formatted(tokenConfig.emailVerification().intervalInMilliseconds() / 1000)
                );
            }
        });
        TokenResult token = tokenService.createEmailVerificationToken(user, user.getEmail());
        emailService.sendEmailUsingTemplate(EmailTemplateRequest.builder()
                .to(user.getEmail())
                .subject(messageSource.getMessage("email.verification.subject", null, LocaleContextHolder.getLocale()))
                .template(EmailTemplates.EMAIL_VERIFICATION)
                .model(Map.of(
                        "recipientName", Objects.requireNonNullElse(user.getUsername(), user.getEmail()),
                        "token", token.rawToken(),
                        "expiryInMinutes", tokenConfig.emailVerification().maxAgeInMilliseconds() / 60000
                ))
                .build()
        );
        return RequestVerifyEmailResponse.builder().email(user.getEmail()).build();
    }

    public ConfirmVerifyEmailResponse verifyEmail(User user, ConfirmVerifyEmailRequest request) {
        Token token = tokenService.validateToken(user, request.token(), TokenType.EMAIL_VERIFICATION);
        user.setEmailVerified(true);
        userRepository.save(user);
        return ConfirmVerifyEmailResponse.builder().email(token.getEmail()).build();
    }
}
