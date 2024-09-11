package me.cyberproton.ocean.features.auth.services;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.auth.dtos.*;
import me.cyberproton.ocean.features.email.EmailService;
import me.cyberproton.ocean.features.email.EmailTemplateRequest;
import me.cyberproton.ocean.features.email.EmailTemplates;
import me.cyberproton.ocean.features.jwt.JwtService;
import me.cyberproton.ocean.features.role.Role;
import me.cyberproton.ocean.features.role.RoleRepository;
import me.cyberproton.ocean.features.token.*;
import me.cyberproton.ocean.features.user.*;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final TokenConfig tokenConfig;
    private final EmailService emailService;
    private final UserMapper userMapper;

    @Qualifier(value = "emailMessageSource") private final MessageSource messageSource;

    private final ApplicationEventPublisher applicationEventPublisher;

    public LoginResponse register(RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email %s is already taken".formatted(registerRequest.getEmail()));
        }
        if (username != null) {
            if (userRepository.existsByUsername(username)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Username %s is already taken".formatted(registerRequest.getUsername()));
            }
        } else {
            // Generate username from email
            Faker faker = new Faker();
            String emailWithoutDomain = registerRequest.getEmail().split("@")[0];
            boolean isUsernameConflict = true;
            username = emailWithoutDomain + faker.number().digits(6);
            for (int numberOfTries = 0; numberOfTries < 5; numberOfTries++) {
                if (!userRepository.existsByUsername(username)) {
                    isUsernameConflict = false;
                    break;
                }
                username = emailWithoutDomain + faker.number().digits(6);
            }
            if (isUsernameConflict) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to generate username from email %s"
                                .formatted(registerRequest.getEmail()));
            }
        }
        Set<Role> roles =
                Set.of(
                        roleRepository
                                .findByName("USER")
                                .orElseThrow(
                                        () ->
                                                new ResponseStatusException(
                                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                                        "Default role USER not found")));
        UserEntity user =
                UserEntity.builder()
                        .email(registerRequest.getEmail())
                        .username(username)
                        .password(passwordEncoder.encode(registerRequest.getPassword()))
                        .roles(roles)
                        .build();
        userRepository.save(user);
        applicationEventPublisher.publishEvent(new UserEvent(UserEvent.Type.CREATE, user));
        emailService.sendEmailUsingTemplate(
                EmailTemplateRequest.builder()
                        .to(user.getEmail())
                        .subject(
                                messageSource.getMessage(
                                        "welcome.subject", null, LocaleContextHolder.getLocale()))
                        .template(EmailTemplates.WELCOME)
                        .model(
                                Map.of(
                                        "recipientName",
                                        Objects.requireNonNullElse(
                                                user.getUsername(), user.getEmail())))
                        .build());
        String token = jwtService.generateToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());
        return LoginResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .user(userMapper.entityToResponse(user))
                .build();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()));
        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
        String accessToken = jwtService.generateToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userMapper.entityToResponse(user))
                .build();
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String email = jwtService.getEmailFromToken(refreshTokenRequest.refreshToken());
        UserEntity user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        String accessToken = jwtService.generateToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());
        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userMapper.entityToResponse(user))
                .build();
    }

    public ResetPasswordResponse requestResetPassword(ResetPasswordRequest resetPasswordRequest) {
        String usernameOrEmail = resetPasswordRequest.getUsernameOrEmail();
        UserEntity user =
                userRepository
                        .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                        .orElseThrow();
        Optional<Token> latestTokenOpt =
                tokenService.findLatestToken(user, TokenType.PASSWORD_RESET);
        latestTokenOpt.ifPresent(
                (latestToken) -> {
                    if (latestToken
                            .getCreatedAt()
                            .toInstant()
                            .plusMillis(tokenConfig.passwordReset().intervalInMilliseconds())
                            .isAfter(Instant.now())) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Please wait for %s seconds before requesting another reset password"
                                        .formatted(
                                                tokenConfig.passwordReset().intervalInMilliseconds()
                                                        / 1000));
                    }
                });
        TokenResult token = tokenService.createToken(user, TokenType.PASSWORD_RESET);
        emailService.sendEmailUsingTemplate(
                EmailTemplateRequest.builder()
                        .to(user.getEmail())
                        .subject(
                                messageSource.getMessage(
                                        "password.reset.subject",
                                        null,
                                        LocaleContextHolder.getLocale()))
                        .template(EmailTemplates.PASSWORD_RESET)
                        .model(
                                Map.of(
                                        "recipientName",
                                        Objects.requireNonNullElse(
                                                user.getUsername(), user.getEmail()),
                                        "token",
                                        token.rawToken(),
                                        "expiryInMinutes",
                                        tokenConfig.passwordReset().maxAgeInMilliseconds() / 60000))
                        .build());
        return ResetPasswordResponse.builder().build();
    }

    public ConfirmResetPasswordResponse confirmResetPassword(
            ConfirmResetPasswordRequest confirmResetPasswordRequest) {
        UserEntity user =
                userRepository
                        .findByUsernameOrEmail(
                                confirmResetPasswordRequest.getUsernameOrEmail(),
                                confirmResetPasswordRequest.getUsernameOrEmail())
                        .orElseThrow();
        tokenService.validateToken(
                user, confirmResetPasswordRequest.getToken(), TokenType.PASSWORD_RESET);
        user.setPassword(passwordEncoder.encode(confirmResetPasswordRequest.getNewPassword()));
        userRepository.save(user);
        return ConfirmResetPasswordResponse.builder()
                .message("Password reset successfully")
                .build();
    }

    public RequestVerifyEmailResponse requestVerifyEmail(UserEntity user) {
        Optional<Token> latestTokenOpt =
                tokenService.findLatestToken(user, TokenType.EMAIL_VERIFICATION);
        latestTokenOpt.ifPresent(
                (latestToken) -> {
                    if (latestToken
                            .getCreatedAt()
                            .toInstant()
                            .plusMillis(tokenConfig.emailVerification().intervalInMilliseconds())
                            .isAfter(Instant.now())) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Please wait for %s seconds before requesting another verify email"
                                        .formatted(
                                                tokenConfig
                                                                .emailVerification()
                                                                .intervalInMilliseconds()
                                                        / 1000));
                    }
                });
        TokenResult token = tokenService.createEmailVerificationToken(user, user.getEmail());
        emailService.sendEmailUsingTemplate(
                EmailTemplateRequest.builder()
                        .to(user.getEmail())
                        .subject(
                                messageSource.getMessage(
                                        "email.verification.subject",
                                        null,
                                        LocaleContextHolder.getLocale()))
                        .template(EmailTemplates.EMAIL_VERIFICATION)
                        .model(
                                Map.of(
                                        "recipientName",
                                        Objects.requireNonNullElse(
                                                user.getUsername(), user.getEmail()),
                                        "token",
                                        token.rawToken(),
                                        "expiryInMinutes",
                                        tokenConfig.emailVerification().maxAgeInMilliseconds()
                                                / 60000))
                        .build());
        return RequestVerifyEmailResponse.builder().email(user.getEmail()).build();
    }

    public ConfirmVerifyEmailResponse verifyEmail(
            UserEntity user, ConfirmVerifyEmailRequest request) {
        Token token =
                tokenService.validateToken(user, request.token(), TokenType.EMAIL_VERIFICATION);
        user.setEmailVerified(true);
        userRepository.save(user);
        return ConfirmVerifyEmailResponse.builder().email(token.getEmail()).build();
    }
}
