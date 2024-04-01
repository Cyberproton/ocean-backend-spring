package me.cyberproton.ocean.features.user;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.config.AppUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<Object> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("following")
    public Set<UserResponse> getFollowing(@AuthenticationPrincipal AppUserDetails userDetails) {
        return userService.getFollowing(userDetails.getUserId());
    }

    @PostMapping("follow/{id}")
    public ResponseEntity<Set<UserResponse>> followUser(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.followUser(userDetails.getUser(), id));
    }

    @PostMapping("unfollow/{id}")
    public ResponseEntity<Set<UserResponse>> unfollowUser(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.unfollowUser(userDetails.getUser(), id));
    }
}
