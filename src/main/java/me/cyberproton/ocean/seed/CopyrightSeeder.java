package me.cyberproton.ocean.seed;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.copyright.Copyright;
import me.cyberproton.ocean.features.copyright.CopyrightRepository;
import me.cyberproton.ocean.features.copyright.CopyrightType;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class CopyrightSeeder {
    private final Faker faker;
    private final CopyrightRepository repository;

    public List<Copyright> seed() {
        List<Copyright> copyrights = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            copyrights.add(Copyright
                    .builder()
                    .type(SeedUtils.randomElement(CopyrightType.values()))
                    .text(faker.lorem().sentence())
                    .build()
            );
        }
        return repository.saveAll(copyrights);
    }
}
