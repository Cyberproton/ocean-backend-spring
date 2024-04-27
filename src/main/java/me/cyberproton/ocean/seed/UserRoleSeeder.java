package me.cyberproton.ocean.seed;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.role.DefaultRole;
import me.cyberproton.ocean.features.role.Role;
import me.cyberproton.ocean.features.role.RoleRepository;
import me.cyberproton.ocean.features.user.User;
import me.cyberproton.ocean.features.user.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Component
public class UserRoleSeeder {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void seed(List<User> users, List<Role> roles) {
        Role userRole = roles.stream().filter(role -> role.getName().equals(DefaultRole.USER.name())).findFirst().orElseThrow();
        List<Role> otherRoles = roles.stream().filter(role -> !role.getName().equals(DefaultRole.USER.name())).toList();
        for (User user : users) {
            List<Role> userRoles = new ArrayList<>();
            userRoles.add(userRole);
            userRoles.addAll(SeedUtils.randomElements(otherRoles, 1));
            user.setRoles(Set.copyOf(userRoles));
        }
        userRepository.saveAll(users);
    }
}
