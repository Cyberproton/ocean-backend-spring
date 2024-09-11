package me.cyberproton.ocean.features.user;

import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(
                        user ->
                                UserResponse.builder()
                                        .id(user.getId())
                                        .username(user.getUsername())
                                        .email(user.getEmail())
                                        .isEmailVerified(user.isEmailVerified())
                                        .isLocked(user.isLocked())
                                        .build())
                .collect(Collectors.toList());
    }

    @Nullable public UserResponse getUserById(long id) {
        UserEntity user = userRepository.findEagerById(id).orElseThrow();
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isEmailVerified(user.isEmailVerified())
                .isLocked(user.isLocked())
                .build();
    }

    public Set<UserResponse> getFollowing(long id) {
        return userRepository.findFollowingByFollowersId(id).orElseThrow().stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toSet());
    }

    public Set<UserResponse> followUser(UserEntity user, long id) {
        UserEntity follower = userRepository.findById(user.getId()).orElseThrow();
        UserEntity following = userRepository.findById(id).orElseThrow();
        follower.addFollowing(following);
        userRepository.save(follower);
        return follower.getFollowing().stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toSet());
    }

    public Set<UserResponse> unfollowUser(UserEntity user, long id) {
        UserEntity follower = userRepository.findById(user.getId()).orElseThrow();
        UserEntity following = userRepository.findById(id).orElseThrow();
        follower.removeFollowing(following);
        userRepository.save(follower);
        return follower.getFollowing().stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toSet());
    }

    public UserResponse updateUserUsername(UserEntity user, String username) {
        user.setUsername(username);
        userRepository.save(user);
        return mapUserToResponse(user);
    }

    private UserResponse mapUserToResponse(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isEmailVerified(user.isEmailVerified())
                .isLocked(user.isLocked())
                .build();
    }
}
