package me.cyberproton.ocean.features.copyright;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CopyrightService {
    private final CopyrightRepository repository;
}
