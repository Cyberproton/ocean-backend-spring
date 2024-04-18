package me.cyberproton.ocean.features.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import me.cyberproton.ocean.domain.PaginationResponse;
import me.cyberproton.ocean.features.album.AlbumResponse;
import me.cyberproton.ocean.features.artist.ArtistResponse;
import me.cyberproton.ocean.features.playlist.PlaylistResponse;
import me.cyberproton.ocean.features.track.TrackResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record SearchResponse(
        PaginationResponse<TrackResponse> tracks,
        PaginationResponse<AlbumResponse> albums,
        PaginationResponse<ArtistResponse> artists,
        PaginationResponse<PlaylistResponse> playlists
) {
}
