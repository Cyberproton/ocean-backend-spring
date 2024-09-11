package me.cyberproton.ocean.seed;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.role.DefaultRole;
import me.cyberproton.ocean.features.role.Role;
import me.cyberproton.ocean.features.role.RoleRepository;
import me.cyberproton.ocean.features.user.UserEntity;
import me.cyberproton.ocean.features.user.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("seeder")
@AllArgsConstructor
@Component
public class UserRoleSeeder {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void seed(List<UserEntity> users, List<Role> roles) {
        Role userRole =
                roles.stream()
                        .filter(role -> role.getName().equals(DefaultRole.USER.name()))
                        .findFirst()
                        .orElseThrow();
        List<Role> otherRoles =
                roles.stream()
                        .filter(role -> !role.getName().equals(DefaultRole.USER.name()))
                        .toList();
        for (UserEntity user : users) {
            List<Role> userRoles = new ArrayList<>();
            userRoles.add(userRole);
            userRoles.addAll(SeedUtils.randomElements(otherRoles, 1));
            user.setRoles(Set.copyOf(userRoles));
        }
        userRepository.saveAll(users);
    }
}
