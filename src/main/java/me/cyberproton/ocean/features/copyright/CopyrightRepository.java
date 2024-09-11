package me.cyberproton.ocean.features.copyright;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CopyrightRepository extends JpaRepository<CopyrightEntity, Long> {}
