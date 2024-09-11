package me.cyberproton.ocean.features.role;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles;
}
