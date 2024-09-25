package me.cyberproton.ocean.features.history.dto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Mapping;

import me.cyberproton.ocean.features.album.view.AlbumView;
import me.cyberproton.ocean.features.history.entity.HistoryEntity;
import me.cyberproton.ocean.features.history.entity.HistoryType;
import me.cyberproton.ocean.features.playlist.view.PlaylistView;
import me.cyberproton.ocean.features.profile.dto.BaseProfileView;
import me.cyberproton.ocean.features.track.view.TrackView;

import java.util.Date;

@EntityView(HistoryEntity.class)
public interface HistoryView {
    Long getId();

    HistoryType getType();

    Date getUpdatedAt();

    TrackView getTrack();

    AlbumView getAlbum();

    @Mapping("artist.user.profile")
    BaseProfileView getArtist();

    PlaylistView getPlaylist();
}
