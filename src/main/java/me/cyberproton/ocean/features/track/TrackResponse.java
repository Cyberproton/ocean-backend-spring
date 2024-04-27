package me.cyberproton.ocean.features.track;

import lombok.Builder;
import me.cyberproton.ocean.features.elasticsearch.TrackDocument;

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

    public static TrackResponse fromElasticsearchDocument(TrackDocument trackDocument) {
        return TrackResponse.builder()
                .id(trackDocument.getId())
                .name(trackDocument.getName())
                .trackNumber(trackDocument.getTrackNumber())
                .duration(trackDocument.getDuration())
                .build();
    }
}
