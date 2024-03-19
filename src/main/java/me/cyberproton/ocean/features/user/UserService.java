package me.cyberproton.ocean.features.user;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
        User user = userRepository.findById(id).orElseThrow();
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isEmailVerified(user.isEmailVerified())
                .isLocked(user.isLocked())
                .build();
    }
}
