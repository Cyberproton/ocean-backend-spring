package me.cyberproton.ocean.features.auth.controllers;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiController;
import me.cyberproton.ocean.features.auth.dtos.*;
import me.cyberproton.ocean.features.auth.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@V1ApiController
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/reset-password/request")
    public ResetPasswordResponse resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        return authService.resetPassword(resetPasswordRequest);
    }

    @PostMapping("/reset-password/confirm")
    public ConfirmResetPasswordResponse confirmResetPassword(@RequestBody ConfirmResetPasswordRequest confirmResetPasswordRequest) {

        return authService.confirmResetPassword(confirmResetPasswordRequest);
    }
}
