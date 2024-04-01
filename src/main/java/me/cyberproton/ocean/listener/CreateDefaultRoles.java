package me.cyberproton.ocean.listener;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.cyberproton.ocean.features.role.Role;
import me.cyberproton.ocean.features.role.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CreateDefaultRoles implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final boolean alreadySetup = false;

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        if (roleRepository.findByName("USER") != null) {
            return;
        }
        roleRepository.saveAll(List.of(
                Role.builder().name("USER").build(),
                Role.builder().name("ARTIST").build(),
                Role.builder().name("ADMIN").build()
        ));
    }
}
