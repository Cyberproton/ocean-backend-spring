package me.cyberproton.ocean.features.album.util;

import me.cyberproton.ocean.features.album.dto.AlbumResponse;
import me.cyberproton.ocean.features.album.entity.AlbumDocument;
import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.album.view.AlbumView;
import me.cyberproton.ocean.features.artist.ArtistMapper;
import me.cyberproton.ocean.features.profile.dto.ProfileResponse;
import me.cyberproton.ocean.util.PersistenceUtils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AlbumMapper {
    @Autowired private ArtistMapper artistMapper;

    @Mapping(target = "artists", expression = "java(getAlbumArtists(albumEntity))")
    public abstract AlbumResponse entityToResponse(AlbumEntity albumEntity);

    public abstract AlbumResponse viewToResponse(AlbumView input);

    public abstract AlbumDocument entityToDocument(AlbumEntity albumEntity);

    public abstract AlbumResponse documentToResponse(AlbumDocument albumDocument);

    protected List<ProfileResponse> getAlbumArtists(AlbumEntity album) {
        return PersistenceUtils.isLoaded(album.getTracks())
                        && album.getTracks().stream()
                                .allMatch(track -> PersistenceUtils.isLoaded(track.getArtists()))
                ? album.getTracks().stream()
                        .flatMap(track -> track.getArtists().stream())
                        .map(artist -> artistMapper.entityToResponse(artist))
                        .toList()
                : null;
    }
}
