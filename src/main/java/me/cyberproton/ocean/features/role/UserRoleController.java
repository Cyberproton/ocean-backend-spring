package me.cyberproton.ocean.features.role;

import java.util.Set;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/users/{userId}/roles")
public class UserRoleController {
    private final RoleService roleService;

    @GetMapping
    public Set<RoleResponse> getUserRoles(@PathVariable Long userId) {
        return roleService.getUserRoles(userId);
    }

    @PutMapping
    public Set<RoleResponse> assignRoles(
            @PathVariable Long userId, @RequestBody UpdateUserRoleRequest request) {
        return roleService.assignUserRoles(userId, request);
    }
}
