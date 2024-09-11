package me.cyberproton.ocean.features.history.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

import me.cyberproton.ocean.features.album.dto.AlbumResponse;
import me.cyberproton.ocean.features.history.entity.HistoryType;
import me.cyberproton.ocean.features.playlist.dto.PlaylistResponse;
import me.cyberproton.ocean.features.profile.dto.ProfileResponse;
import me.cyberproton.ocean.features.track.dto.TrackResponse;
import me.cyberproton.ocean.features.user.UserResponse;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class HistoryResponse {
    private Long id;
    private HistoryType type;
    private Date updatedAt;
    private TrackResponse track;
    private AlbumResponse album;
    private ProfileResponse artist;
    private PlaylistResponse playlist;
    private UserResponse user;
}
