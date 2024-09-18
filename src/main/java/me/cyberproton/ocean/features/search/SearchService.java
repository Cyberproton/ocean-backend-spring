package me.cyberproton.ocean.features.search;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.config.ExternalAppConfig;
import me.cyberproton.ocean.domain.PaginationResponse;
import me.cyberproton.ocean.features.album.dto.AlbumResponse;
import me.cyberproton.ocean.features.album.entity.AlbumDocument;
import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.album.repository.AlbumElasticRepository;
import me.cyberproton.ocean.features.album.repository.AlbumRepository;
import me.cyberproton.ocean.features.album.util.AlbumMapper;
import me.cyberproton.ocean.features.artist.ArtistDocument;
import me.cyberproton.ocean.features.artist.ArtistMapper;
import me.cyberproton.ocean.features.playlist.dto.PlaylistResponse;
import me.cyberproton.ocean.features.playlist.entity.PlaylistDocument;
import me.cyberproton.ocean.features.playlist.entity.PlaylistEntity;
import me.cyberproton.ocean.features.playlist.repository.PlaylistElasticRepository;
import me.cyberproton.ocean.features.playlist.repository.PlaylistRepository;
import me.cyberproton.ocean.features.playlist.util.PlaylistMapper;
import me.cyberproton.ocean.features.profile.dto.ProfileResponse;
import me.cyberproton.ocean.features.track.dto.TrackResponse;
import me.cyberproton.ocean.features.track.entity.TrackDocument;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.track.repository.CustomTrackElasticRepository;
import me.cyberproton.ocean.features.track.repository.TrackRepository;
import me.cyberproton.ocean.features.track.util.TrackMapper;
import me.cyberproton.ocean.features.user.UserDocument;
import me.cyberproton.ocean.features.user.UserElasticRepository;
import me.cyberproton.ocean.features.user.UserEntity;
import me.cyberproton.ocean.features.user.UserRepository;
import me.cyberproton.ocean.util.ImageUrlMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@AllArgsConstructor
@Service
public class SearchService {
    private final ExternalAppConfig externalAppConfig;
    private final ElasticsearchOperations elasticsearchOperations;
    private final AlbumRepository albumRepository;
    private final AlbumElasticRepository albumElasticRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistElasticRepository playlistElasticRepository;
    private final UserElasticRepository userElasticRepository;
    private final TrackRepository trackRepository;
    private final CustomTrackElasticRepository customTrackElasticRepository;
    private final ImageUrlMapper imageUrlMapper;
    private final AlbumMapper albumMapper;
    private final ArtistMapper artistMapper;
    private final PlaylistMapper playlistMapper;
    private final TrackMapper trackMapper;
    private final UserRepository userRepository;

    public SearchResponse search(SearchQuery query) {
        Pageable pageable = PageRequest.of(query.getOffset(), query.getLimit());
        List<Query> queries = new ArrayList<>();
        List<Class<?>> documentClasses = new ArrayList<>();
        Set<SearchQuery.Type> type = query.getType();
        if (type == null || type.isEmpty()) {
            type =
                    Set.of(
                            SearchQuery.Type.ALBUM,
                            SearchQuery.Type.ARTIST,
                            SearchQuery.Type.PLAYLIST,
                            SearchQuery.Type.TRACK);
        }
        if (type.contains(SearchQuery.Type.ALBUM)) {
            Query albumQuery = createQuery(query, "name", pageable);
            queries.add(albumQuery);
            documentClasses.add(AlbumDocument.class);
        }
        if (type.contains(SearchQuery.Type.ARTIST)) {
            Query artistQuery = createQuery(query, "profile.name", pageable);
            queries.add(artistQuery);
            documentClasses.add(UserDocument.class);
        }
        if (type.contains(SearchQuery.Type.PLAYLIST)) {
            Query playlistQuery = createQuery(query, "name", pageable);
            queries.add(playlistQuery);
            documentClasses.add(PlaylistDocument.class);
        }
        if (type.contains(SearchQuery.Type.TRACK)) {
            Query trackQuery = createQuery(query, "name", pageable);
            queries.add(trackQuery);
            documentClasses.add(TrackDocument.class);
        }

        List<SearchHits<?>> hits = elasticsearchOperations.multiSearch(queries, documentClasses);

        PaginationResponse<AlbumResponse> albums = null;
        PaginationResponse<ProfileResponse> artists = null;
        PaginationResponse<PlaylistResponse> playlists = null;
        PaginationResponse<TrackResponse> tracks = null;
        if (type.contains(SearchQuery.Type.ALBUM)) {
            long totalHits = hits.getFirst().getTotalHits();
            System.out.println("totalHits: " + totalHits);
            albums =
                    fromSearchHits(
                            hits.getFirst(),
                            (hit) -> albumMapper.documentToResponse((AlbumDocument) hit),
                            query,
                            SearchQuery.Type.ALBUM);
            hits.removeFirst();
        }
        if (type.contains(SearchQuery.Type.ARTIST)) {
            artists =
                    fromSearchHits(
                            hits.getFirst(),
                            (hit) -> artistMapper.documentToResponse((UserDocument) hit),
                            query,
                            SearchQuery.Type.ARTIST);
            hits.removeFirst();
        }
        if (type.contains(SearchQuery.Type.PLAYLIST)) {
            playlists =
                    fromSearchHits(
                            hits.getFirst(),
                            (hit) -> playlistMapper.documentToResponse((PlaylistDocument) hit),
                            query,
                            SearchQuery.Type.PLAYLIST);
            hits.removeFirst();
        }
        if (type.contains(SearchQuery.Type.TRACK)) {
            tracks =
                    fromSearchHits(
                            hits.getFirst(),
                            (hit) -> trackMapper.documentToResponse((TrackDocument) hit),
                            query,
                            SearchQuery.Type.TRACK);
            hits.removeFirst();
        }
        return SearchResponse.builder()
                .albums(albums)
                .artists(artists)
                .playlists(playlists)
                .tracks(tracks)
                .build();
    }

    private Query createQuery(SearchQuery query, String queryForField, Pageable pageable) {
        return query.getQuery() == null || query.getQuery().trim().isEmpty()
                ? Query.findAll().setPageable(pageable)
                : new CriteriaQuery(new Criteria(queryForField).fuzzy(query.getQuery()), pageable);
    }

    private <T> PaginationResponse<T> fromSearchHits(
            SearchHits<?> searchHits,
            Function<Object, T> mapper,
            SearchQuery query,
            SearchQuery.Type type) {
        String next = null;
        String prev = null;
        boolean hasNext = searchHits.getTotalHits() > query.getOffset() + query.getLimit();
        boolean hasPrev = query.getOffset() > 0;
        if (hasNext) {
            next =
                    externalAppConfig.domain()
                            + "/search"
                            + "?type="
                            + type
                            + "?query="
                            + query.getQuery()
                            + "&limit="
                            + query.getLimit()
                            + "&offset="
                            + (query.getOffset() + query.getLimit());
        }
        if (hasPrev) {
            prev =
                    externalAppConfig.domain()
                            + "/search"
                            + "?type="
                            + type
                            + "?query="
                            + query.getQuery()
                            + "&limit="
                            + query.getLimit()
                            + "&offset="
                            + Math.max(0, query.getOffset() - query.getLimit());
        }
        return PaginationResponse.fromSearchHits(
                searchHits, mapper, query.getLimit(), query.getOffset(), next, prev);
    }

    public void refreshIndices() {
        List<Class<?>> indexClasses =
                List.of(
                        AlbumDocument.class,
                        ArtistDocument.class,
                        PlaylistDocument.class,
                        TrackDocument.class);
        indexClasses.forEach(
                indexClass -> {
                    IndexOperations indexOperations = elasticsearchOperations.indexOps(indexClass);
                    if (indexOperations.exists()) {
                        indexOperations.delete();
                    }
                    if (!indexOperations.exists()) {
                        indexOperations.create();
                    }
                });
        Page<AlbumEntity> albumPage = albumRepository.findAll(PageRequest.of(0, 20));
        do {
            albumElasticRepository.saveAll(albumPage);
            albumPage = albumRepository.findAll(albumPage.nextPageable());
        } while (albumPage.hasNext());
        Page<UserEntity> artistPage = userRepository.findAllByArtistNotNull(PageRequest.of(0, 20));
        do {
            userElasticRepository.saveAll(artistPage);
            artistPage = userRepository.findAllByArtistNotNull(artistPage.nextPageable());
        } while (artistPage.hasNext());
        Page<PlaylistEntity> playlistPage = playlistRepository.findAll(PageRequest.of(0, 20));
        do {
            playlistElasticRepository.saveAll(playlistPage);
            playlistPage = playlistRepository.findAll(playlistPage.nextPageable());
        } while (playlistPage.hasNext());
        Page<TrackEntity> trackPage = trackRepository.findAll(PageRequest.of(0, 20));
        do {
            customTrackElasticRepository.saveAll(trackPage);
            trackPage = trackRepository.findAll(trackPage.nextPageable());
        } while (trackPage.hasNext());
    }
}
