package me.cyberproton.ocean.features.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;

    @JsonProperty(value = "isLocked")
    private boolean isLocked;

    @JsonProperty(value = "isEmailVerified")
    private boolean isEmailVerified;

    private Set<UserResponse> following;
    private Set<UserResponse> followers;

    //    public static UserResponse fromUser(UserEntity user, ConversionOptions options) {
    //        return UserResponse.builder()
    //                .id(user.getId())
    //                .username(user.getUsername())
    //                .email(user.getEmail())
    //                .isLocked(user.isLocked())
    //                .isEmailVerified(user.isEmailVerified())
    //                .following(
    //                        PersistenceUtils.isLoaded(user.getFollowing())
    //                                ? user.getFollowing().stream()
    //                                        .map(UserResponse::fromUser)
    //                                        .collect(Collectors.toSet())
    //                                : null)
    //                .followers(
    //                        PersistenceUtils.isLoaded(user.getFollowers())
    //                                ? user.getFollowers().stream()
    //                                        .map(UserResponse::fromUser)
    //                                        .collect(Collectors.toSet())
    //                                : null)
    //                .build();
    //    }
    //
    //    public static UserResponse fromUser(UserEntity user) {
    //        return fromUser(user, new ConversionOptions(false, false, false));
    //    }
    //
    //    @Builder
    //    public record ConversionOptions(
    //            boolean excludeRelations, boolean excludeFollowing, boolean excludeFollowers) {}
}
