package me.cyberproton.ocean.seed;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.role.DefaultRole;
import me.cyberproton.ocean.features.role.Role;
import me.cyberproton.ocean.features.role.RoleRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Profile("seeder")
public class RoleSeeder {
    private final RoleRepository roleRepository;

    public List<Role> seed() {
        List<Role> roles =
                Arrays.stream(DefaultRole.values())
                        .map(defaultRole -> Role.builder().name(defaultRole.name()).build())
                        .toList();
        return roleRepository.saveAll(roles);
    }
}
