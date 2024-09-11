package me.cyberproton.ocean.listener;

import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.cyberproton.ocean.features.role.Role;
import me.cyberproton.ocean.features.role.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CreateDefaultRoles implements ApplicationListener<ContextRefreshedEvent> {
    private static boolean alreadySetup = false;
    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public void onApplicationEvent(@Nonnull ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        if (roleRepository.findByName("USER").orElse(null) != null) {
            return;
        }
        roleRepository.saveAll(
                List.of(
                        Role.builder().name("USER").build(),
                        Role.builder().name("ARTIST").build(),
                        Role.builder().name("ADMIN").build()));
        alreadySetup = true;
    }
}
