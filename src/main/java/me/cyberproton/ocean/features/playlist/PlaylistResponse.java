package me.cyberproton.ocean.features.playlist;

import me.cyberproton.ocean.features.file.ImageResponse;
import me.cyberproton.ocean.util.ImageUrlMapper;

public record PlaylistResponse(
        Long id,
        String name,
        String description,
        ImageResponse cover,
        Long totalTracks
) {
    public static PlaylistResponse fromEntity(Playlist playlist, ImageUrlMapper imageUrlMapper) {
        return new PlaylistResponse(
                playlist.getId(),
                playlist.getName(),
                playlist.getDescription(),
                playlist.getCover() != null
                        ? new ImageResponse(imageUrlMapper.mapFileToUrl(playlist.getCover()))
                        : null,
                playlist.getPlaylistTracks() == null ? 0 : (long) playlist.getPlaylistTracks().size()
        );
    }

    public static PlaylistResponse fromElasticsearchDocument(PlaylistDocument playlistDocument, ImageUrlMapper imageUrlMapper) {
        return new PlaylistResponse(
                playlistDocument.getId(),
                playlistDocument.getName(),
                playlistDocument.getDescription(),
                playlistDocument.getCoverId() != null
                        ? new ImageResponse(imageUrlMapper.mapFileIdToUrl(playlistDocument.getCoverId()))
                        : null,
                null
        );
    }
}
