package me.cyberproton.ocean.features.token;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.cyberproton.ocean.features.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue
    private Long id;

    private TokenType type;

    private String token;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @ManyToOne
    private User user;
}
