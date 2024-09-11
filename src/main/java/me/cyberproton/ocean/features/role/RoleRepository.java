package me.cyberproton.ocean.features.role;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    Optional<Set<Role>> findRolesByNameIn(Collection<String> roleNames);
}
