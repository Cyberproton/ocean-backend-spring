package me.cyberproton.ocean.features.role;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.user.User;
import me.cyberproton.ocean.features.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RoleService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Set<RoleResponse> getUserRoles(long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getRoles().stream().map(this::mapToRoleResponse).collect(Collectors.toSet());
    }

    @Transactional
    public Set<RoleResponse> assignUserRoles(long userId, UpdateUserRoleRequest request) {
        User user = userRepository.findById(userId).orElseThrow();
        Set<Role> roles = roleRepository.findRolesByNameIn(request.roles()).orElseThrow();
        if (roles.stream().noneMatch(role -> role.getName().equals("USER"))) {
            roles.add(roleRepository.findByName("USER").orElseThrow());
        }
        user.setRoles(roles);
        userRepository.save(user);
        return roles.stream().map(this::mapToRoleResponse).collect(Collectors.toSet());
    }

    private RoleResponse mapToRoleResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
