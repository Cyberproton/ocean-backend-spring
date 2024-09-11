package me.cyberproton.ocean.features.recordlabel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateOrUpdateRecordLabelRequest(
        @NotBlank @Size(max = RecordLabelConstants.MAX_NAME_LENGTH) String name) {}
