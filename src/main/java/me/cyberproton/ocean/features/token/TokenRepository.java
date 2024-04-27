package me.cyberproton.ocean.features.token;

import me.cyberproton.ocean.features.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findFirstByUserAndTypeOrderByCreatedAtDesc(User user, TokenType type);
}
