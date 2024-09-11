package me.cyberproton.ocean.features.role;

import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.user.UserEntity;
import me.cyberproton.ocean.features.user.UserRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RoleService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Set<RoleResponse> getUserRoles(long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        return user.getRoles().stream().map(this::mapToRoleResponse).collect(Collectors.toSet());
    }

    @Transactional
    public Set<RoleResponse> assignUserRoles(long userId, UpdateUserRoleRequest request) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        Set<Role> roles = roleRepository.findRolesByNameIn(request.roles()).orElseThrow();
        if (roles.stream().noneMatch(role -> role.getName().equals("USER"))) {
            roles.add(roleRepository.findByName("USER").orElseThrow());
        }
        user.setRoles(roles);
        userRepository.save(user);
        return roles.stream().map(this::mapToRoleResponse).collect(Collectors.toSet());
    }

    private RoleResponse mapToRoleResponse(Role role) {
        return RoleResponse.builder().id(role.getId()).name(role.getName()).build();
    }
}
