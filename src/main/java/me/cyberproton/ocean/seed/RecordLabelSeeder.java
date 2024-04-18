package me.cyberproton.ocean.seed;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.recordlabel.RecordLabel;
import me.cyberproton.ocean.features.recordlabel.RecordLabelRepository;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class RecordLabelSeeder {
    private final Faker faker;
    private final RecordLabelRepository recordLabelRepository;

    public List<RecordLabel> seed() {
        int numberOfRecordLabels = 10;

        List<RecordLabel> recordLabels = new ArrayList<>();
        for (int i = 0; i < numberOfRecordLabels; i++) {
            recordLabels.add(
                    RecordLabel.builder()
                            .name(faker.company().name())
                            .build()
            );
        }

        return recordLabelRepository.saveAll(recordLabels);
    }
}
