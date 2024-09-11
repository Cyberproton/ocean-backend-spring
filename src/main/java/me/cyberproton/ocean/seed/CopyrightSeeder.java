package me.cyberproton.ocean.seed;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.copyright.CopyrightEntity;
import me.cyberproton.ocean.features.copyright.CopyrightRepository;
import me.cyberproton.ocean.features.copyright.CopyrightType;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Profile("seeder")
public class CopyrightSeeder {
    private final Faker faker;
    private final CopyrightRepository repository;

    public List<CopyrightEntity> seed() {
        List<CopyrightEntity> copyrights = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            copyrights.add(
                    CopyrightEntity.builder()
                            .type(SeedUtils.randomElement(CopyrightType.values()))
                            .text(faker.lorem().sentence())
                            .build());
        }
        return repository.saveAll(copyrights);
    }
}
