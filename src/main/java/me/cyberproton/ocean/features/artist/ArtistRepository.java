package me.cyberproton.ocean.features.artist;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    @EntityGraph(attributePaths = "user.profile")
    Optional<Artist> findWithProfileById(Long id);

    Optional<Artist> findFirstByUserId(Long userId);

    Optional<Artist> findFirstByUserProfileId(Long profileId);
}
