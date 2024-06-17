package me.cyberproton.ocean.features.user;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                             .map(user -> UserResponse.builder()
                                                      .id(user.getId())
                                                      .username(user.getUsername())
                                                      .email(user.getEmail())
                                                      .isEmailVerified(user.isEmailVerified())
                                                      .isLocked(user.isLocked())
                                                      .build())
                             .collect(Collectors.toList());
    }

    @Nullable
    public UserResponse getUserById(long id) {
        User user = userRepository.findEagerById(id).orElseThrow();
        return UserResponse.builder()
                           .id(user.getId())
                           .username(user.getUsername())
                           .email(user.getEmail())
                           .isEmailVerified(user.isEmailVerified())
                           .isLocked(user.isLocked())
                           .build();
    }

    public Set<UserResponse> getFollowing(long id) {
        return userRepository
                       .findFollowingByFollowersId(id)
                       .orElseThrow()
                       .stream()
                       .map(this::mapUserToResponse)
                       .collect(Collectors.toSet());
    }

    public Set<UserResponse> followUser(User user, long id) {
        User follower = userRepository.findById(user.getId()).orElseThrow();
        User following = userRepository.findById(id).orElseThrow();
        follower.addFollowing(following);
        userRepository.save(follower);
        return follower.getFollowing().stream()
                       .map(this::mapUserToResponse)
                       .collect(Collectors.toSet());
    }

    public Set<UserResponse> unfollowUser(User user, long id) {
        User follower = userRepository.findById(user.getId()).orElseThrow();
        User following = userRepository.findById(id).orElseThrow();
        follower.removeFollowing(following);
        userRepository.save(follower);
        return follower.getFollowing().stream()
                       .map(this::mapUserToResponse)
                       .collect(Collectors.toSet());
    }

    public UserResponse updateUserUsername(User user, String username) {
        user.setUsername(username);
        userRepository.save(user);
        return mapUserToResponse(user);
    }

    private UserResponse mapUserToResponse(User user) {
        return UserResponse.builder()
                           .id(user.getId())
                           .username(user.getUsername())
                           .email(user.getEmail())
                           .isEmailVerified(user.isEmailVerified())
                           .isLocked(user.isLocked())
                           .build();
    }
}
