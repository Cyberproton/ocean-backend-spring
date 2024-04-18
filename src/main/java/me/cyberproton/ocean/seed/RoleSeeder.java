package me.cyberproton.ocean.seed;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.role.DefaultRole;
import me.cyberproton.ocean.features.role.Role;
import me.cyberproton.ocean.features.role.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Component
public class RoleSeeder {
    private final RoleRepository roleRepository;

    public List<Role> seed() {
        List<Role> roles = Arrays.stream(DefaultRole.values()).map(
                defaultRole -> Role.builder()
                        .name(defaultRole.name())
                        .build()
        ).toList();
        return roleRepository.saveAll(roles);
    }
}
