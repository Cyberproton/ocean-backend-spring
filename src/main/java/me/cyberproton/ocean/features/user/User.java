package me.cyberproton.ocean.features.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "_user") // user is a reserved keyword in PostgreSQL
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;


    @Column(unique = true)
    private String username;

    private String password;

    private String email;

    private boolean isLocked;

    private boolean isEmailVerified;
}
