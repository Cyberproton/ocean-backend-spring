package me.cyberproton.ocean.features.auth.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.config.AppUserDetails;
import me.cyberproton.ocean.features.auth.dtos.*;
import me.cyberproton.ocean.features.auth.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public LoginResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/verify-email/request")
    public RequestVerifyEmailResponse verifyEmail(
            @AuthenticationPrincipal AppUserDetails userDetails) {
        return authService.requestVerifyEmail(userDetails.getUser());
    }

    @PostMapping("/verify-email/confirm")
    public ConfirmVerifyEmailResponse verifyEmail(
            @RequestBody ConfirmVerifyEmailRequest request,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        return authService.verifyEmail(userDetails.getUser(), request);
    }

    @PostMapping("/reset-password/request")
    public ResetPasswordResponse requestResetPassword(
            @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return authService.requestResetPassword(resetPasswordRequest);
    }

    @PostMapping("/reset-password/confirm")
    public ConfirmResetPasswordResponse confirmResetPassword(
            @RequestBody ConfirmResetPasswordRequest confirmResetPasswordRequest) {

        return authService.confirmResetPassword(confirmResetPasswordRequest);
    }
}
