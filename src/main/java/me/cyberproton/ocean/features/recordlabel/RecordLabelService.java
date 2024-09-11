package me.cyberproton.ocean.features.recordlabel;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
public class RecordLabelService {
    private final RecordLabelRepository recordLabelRepository;

    public Set<RecordLabelResponse> getRecordLabels() {
        return recordLabelRepository.findAll().stream()
                .map(RecordLabelResponse::fromEntity)
                .collect(Collectors.toSet());
    }

    public RecordLabelResponse getRecordLabelById(Long id) {
        RecordLabelEntity recordLabel =
                recordLabelRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Record label not found"));
        return RecordLabelResponse.fromEntity(recordLabel);
    }

    public RecordLabelResponse createRecordLabel(CreateOrUpdateRecordLabelRequest request) {
        RecordLabelEntity recordLabel = RecordLabelEntity.builder().name(request.name()).build();
        recordLabelRepository.save(recordLabel);
        return RecordLabelResponse.fromEntity(recordLabel);
    }

    public RecordLabelResponse updateRecordLabel(
            Long id, CreateOrUpdateRecordLabelRequest request) {
        RecordLabelEntity recordLabel =
                recordLabelRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Record label not found"));
        recordLabel.setName(request.name());
        recordLabelRepository.save(recordLabel);
        return RecordLabelResponse.fromEntity(recordLabel);
    }

    public void deleteRecordLabel(Long id) {
        RecordLabelEntity recordLabel =
                recordLabelRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Record label not found"));
        recordLabelRepository.delete(recordLabel);
    }
}
