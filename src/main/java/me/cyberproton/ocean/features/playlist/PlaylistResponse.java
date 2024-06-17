package me.cyberproton.ocean.features.playlist;

import me.cyberproton.ocean.features.file.ImageResponse;
import me.cyberproton.ocean.util.ImageUrlMapper;

import java.util.List;

public record PlaylistResponse(
        Long id,
        String name,
        String description,
        List<ImageResponse> covers,
        Long totalTracks
) {
    public static PlaylistResponse fromEntity(Playlist playlist, ImageUrlMapper imageUrlMapper) {
        return new PlaylistResponse(
                playlist.getId(),
                playlist.getName(),
                playlist.getDescription(),
                playlist.getCovers() == null
                        ? null
                        : playlist.getCovers().stream()
                                  .map(fileEntity -> ImageResponse.fromEntity(fileEntity, imageUrlMapper))
                                  .toList(),
                playlist.getPlaylistTracks() == null ? 0 : (long) playlist.getPlaylistTracks().size()
        );
    }

    public static PlaylistResponse fromElasticsearchDocument(
            PlaylistDocument playlistDocument, ImageUrlMapper imageUrlMapper
    ) {
        return new PlaylistResponse(
                playlistDocument.getId(),
                playlistDocument.getName(),
                playlistDocument.getDescription(),
                playlistDocument.getCovers() != null
                        ? playlistDocument.getCovers().stream()
                                          .map(fileDocument -> ImageResponse.fromElasticsearchDocument(
                                                  fileDocument, imageUrlMapper))
                                          .toList()
                        : null,
                null
        );
    }
}
