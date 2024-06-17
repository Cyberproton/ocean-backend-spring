package me.cyberproton.ocean.features.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private boolean isLocked;
    private boolean isEmailVerified;
    private Set<UserResponse> following;
    private Set<UserResponse> followers;

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                           .id(user.getId())
                           .username(user.getUsername())
                           .email(user.getEmail())
                           .isLocked(user.isLocked())
                           .isEmailVerified(user.isEmailVerified())
                           .following(
                                   user.getFollowing() != null ? user.getFollowing().stream()
                                                                     .map(UserResponse::fromUser)
                                                                     .collect(Collectors.toSet()) : null)
                           .followers(
                                   user.getFollowers() != null ? user.getFollowers().stream()
                                                                     .map(UserResponse::fromUser)
                                                                     .collect(Collectors.toSet()) : null)
                           .build();
    }
}
