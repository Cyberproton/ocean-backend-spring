package me.cyberproton.ocean.features.recordlabel;

import jakarta.validation.Valid;
import java.util.Set;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/record-labels")
public class RecordLabelController {
    private final RecordLabelService recordLabelService;

    @GetMapping
    public Set<RecordLabelResponse> getRecordLabels() {
        return recordLabelService.getRecordLabels();
    }

    @GetMapping("/{id}")
    public RecordLabelResponse getRecordLabelById(@PathVariable Long id) {
        return recordLabelService.getRecordLabelById(id);
    }

    @PostMapping
    public RecordLabelResponse createRecordLabel(
            @Valid @RequestBody CreateOrUpdateRecordLabelRequest request) {
        return recordLabelService.createRecordLabel(request);
    }

    @PutMapping("/{id}")
    public RecordLabelResponse updateRecordLabel(
            @PathVariable Long id, @Valid @RequestBody CreateOrUpdateRecordLabelRequest request) {
        return recordLabelService.updateRecordLabel(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteRecordLabel(@PathVariable Long id) {
        recordLabelService.deleteRecordLabel(id);
    }
}
