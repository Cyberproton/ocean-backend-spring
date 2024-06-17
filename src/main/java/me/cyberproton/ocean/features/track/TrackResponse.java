package me.cyberproton.ocean.features.track;

import lombok.Builder;
import me.cyberproton.ocean.features.album.AlbumResponse;
import me.cyberproton.ocean.features.artist.ArtistResponse;
import me.cyberproton.ocean.features.elasticsearch.TrackDocument;
import me.cyberproton.ocean.util.ImageUrlMapper;

import java.util.List;

@Builder
public record TrackResponse(Long id, String name, Integer trackNumber, Integer duration, AlbumResponse album,
                            List<ArtistResponse> artists) {
    public static TrackResponse fromEntity(Track track, ImageUrlMapper imageUrlMapper) {
        return TrackResponse.builder()
                            .id(track.getId())
                            .name(track.getName())
                            .trackNumber(track.getTrackNumber())
                            .duration(track.getDuration())
                            .album(AlbumResponse.fromEntity(track.getAlbum(), imageUrlMapper))
                            .artists(track.getArtists().stream().map(ArtistResponse::fromEntity).toList())
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
