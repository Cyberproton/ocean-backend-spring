package me.cyberproton.ocean.features.recordlabel;

import lombok.Builder;

@Builder
public record RecordLabelResponse(Long id, String name) {
    public static RecordLabelResponse fromEntity(RecordLabelEntity recordLabel) {
        return RecordLabelResponse.builder()
                .id(recordLabel.getId())
                .name(recordLabel.getName())
                .build();
    }
}
