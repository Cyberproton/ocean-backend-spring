package me.cyberproton.ocean.features.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.cyberproton.ocean.features.user.UserConstants;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Email private String email;

    @Size(
            min = UserConstants.USERNAME_MIN_LENGTH,
            max = UserConstants.USERNAME_MAX_LENGTH,
            message = UserConstants.USERNAME_LENGTH_ERROR_MESSAGE)
    private String username;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
