package me.cyberproton.ocean.seed;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.recordlabel.RecordLabelEntity;
import me.cyberproton.ocean.features.recordlabel.RecordLabelRepository;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Profile("seeder")
public class RecordLabelSeeder {
    private final Faker faker;
    private final RecordLabelRepository recordLabelRepository;

    public List<RecordLabelEntity> seed() {
        int numberOfRecordLabels = 10;

        List<RecordLabelEntity> recordLabels = new ArrayList<>();
        for (int i = 0; i < numberOfRecordLabels; i++) {
            recordLabels.add(RecordLabelEntity.builder().name(faker.company().name()).build());
        }

        return recordLabelRepository.saveAll(recordLabels);
    }
}
