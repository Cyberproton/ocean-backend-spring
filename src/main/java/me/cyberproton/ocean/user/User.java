package me.cyberproton.ocean.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
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
