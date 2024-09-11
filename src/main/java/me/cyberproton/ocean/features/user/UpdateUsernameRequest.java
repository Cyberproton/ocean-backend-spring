package me.cyberproton.ocean.features.user;

import jakarta.validation.constraints.Size;

public record UpdateUsernameRequest(
        @Size(
                        min = UserConstants.USERNAME_MIN_LENGTH,
                        max = UserConstants.USERNAME_MAX_LENGTH,
                        message = UserConstants.USERNAME_LENGTH_ERROR_MESSAGE)
                String username) {}
