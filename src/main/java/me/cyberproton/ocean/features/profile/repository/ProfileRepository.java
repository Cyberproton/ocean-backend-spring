package me.cyberproton.ocean.features.profile.repository;

import me.cyberproton.ocean.features.profile.entity.ProfileEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    Optional<ProfileEntity> findByUserId(Long userId);
}
