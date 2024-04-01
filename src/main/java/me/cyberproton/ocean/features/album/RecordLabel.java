package me.cyberproton.ocean.features.album;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity
public class RecordLabel {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany
    private Set<Album> albums;
}
