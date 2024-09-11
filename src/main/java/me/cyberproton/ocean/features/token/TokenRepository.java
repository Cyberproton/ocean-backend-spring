package me.cyberproton.ocean.features.token;

import java.util.Optional;
import me.cyberproton.ocean.features.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findFirstByUserAndTypeOrderByCreatedAtDesc(UserEntity user, TokenType type);
}
