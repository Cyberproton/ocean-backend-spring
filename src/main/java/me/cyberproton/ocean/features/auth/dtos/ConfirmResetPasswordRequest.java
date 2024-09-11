package me.cyberproton.ocean.features.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmResetPasswordRequest {
    private String usernameOrEmail;
    private String token;
    private String newPassword;
}
