package me.cyberproton.ocean.features.album.service;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.album.repository.AlbumRepository;
import me.cyberproton.ocean.features.album.repository.AlbumViewRepository;
import me.cyberproton.ocean.features.album.util.AlbumMapper;
import me.cyberproton.ocean.features.album.view.AlbumView;
import me.cyberproton.ocean.features.user.UserEntity;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class UserAlbumService {
    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final AlbumViewRepository albumViewRepository;

    public List<AlbumView> getSavedAlbums(UserEntity user, BaseQuery query) {
        return StreamSupport.stream(albumViewRepository.findAll().spliterator(), false).toList();
        //        return albumRepository
        //                .findAllBySavedUsersContains(
        //                        user,
        //                        query.toOffsetBasedPageable(),
        //                        DynamicEntityGraph.fetching()
        //                                .addPath("covers")
        //                                .addPath("copyrights")
        //                                .addPath("recordLabel")
        //                                .addPath("tracks", "artists")
        //                                .addPath("tracks", "artists")
        //                                .build())
        //                .map(albumMapper::entityToResponse)
        //                .getContent();
    }

    public void saveAlbums(UserEntity user, Set<Long> albumIds) {
        List<AlbumEntity> albums = albumRepository.findAllById(albumIds);
        if (albums.size() != albumIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Some albums do not exist");
        }
        albums.forEach(album -> album.addSavedUser(user));
        albumRepository.saveAll(albums);
    }

    public void deleteSavedAlbums(UserEntity user, Set<Long> albumIds) {
        List<AlbumEntity> albums = albumRepository.findAllById(albumIds);
        if (albums.size() != albumIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Some albums do not exist");
        }
        albums.forEach(album -> album.removeSavedUser(user));
        albumRepository.saveAll(albums);
    }
}
