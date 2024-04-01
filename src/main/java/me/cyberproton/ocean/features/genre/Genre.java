package me.cyberproton.ocean.features.genre;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Genre {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;
}
