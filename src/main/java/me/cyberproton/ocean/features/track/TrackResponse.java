package me.cyberproton.ocean.features.track;

import lombok.Builder;

@Builder
public record TrackResponse(Long id, String name, Integer trackNumber, Long duration) {
    public static TrackResponse fromEntity(Track track) {
        return TrackResponse.builder()
                .id(track.getId())
                .name(track.getName())
                .trackNumber(track.getTrackNumber())
                .duration(track.getDuration())
                .build();
    }
}
