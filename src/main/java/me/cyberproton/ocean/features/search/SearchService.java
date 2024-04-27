package me.cyberproton.ocean.features.search;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.config.ExternalAppConfig;
import me.cyberproton.ocean.domain.PaginationResponse;
import me.cyberproton.ocean.features.album.AlbumDocument;
import me.cyberproton.ocean.features.album.AlbumResponse;
import me.cyberproton.ocean.features.artist.ArtistDocument;
import me.cyberproton.ocean.features.artist.ArtistResponse;
import me.cyberproton.ocean.features.elasticsearch.TrackDocument;
import me.cyberproton.ocean.features.playlist.PlaylistDocument;
import me.cyberproton.ocean.features.playlist.PlaylistResponse;
import me.cyberproton.ocean.features.track.TrackResponse;
import me.cyberproton.ocean.util.ImageUrlMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class SearchService {
    private final ExternalAppConfig externalAppConfig;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ImageUrlMapper imageUrlMapper;

    public SearchResponse search(SearchQuery query) {
        Pageable pageable = PageRequest.of(query.getOffset(), query.getLimit());
        List<Query> queries = new ArrayList<>();
        List<Class<?>> documentClasses = new ArrayList<>();
        Set<SearchQuery.Type> type = query.getType();
        if (type != null && !type.isEmpty()) {
            if (type.contains(SearchQuery.Type.ALBUM)) {
                Query albumQuery = query.getQuery().trim().isEmpty()
                        ? Query.findAll().setPageable(pageable)
                        : new CriteriaQuery(
                        new Criteria("name").is(query.getQuery()),
                        pageable);
                queries.add(albumQuery);
                documentClasses.add(AlbumDocument.class);
            }
            if (type.contains(SearchQuery.Type.ARTIST)) {
                Query artistQuery = query.getQuery().trim().isEmpty()
                        ? Query.findAll().setPageable(pageable)
                        : new CriteriaQuery(
                        new Criteria("profileName").is(query.getQuery()),
                        pageable);
                queries.add(artistQuery);
                documentClasses.add(ArtistDocument.class);
            }
            if (type.contains(SearchQuery.Type.PLAYLIST)) {
                Query playlistQuery = query.getQuery().trim().isEmpty()
                        ? Query.findAll().setPageable(pageable)
                        : new CriteriaQuery(
                        new Criteria("name").is(query.getQuery()),
                        pageable);
                queries.add(playlistQuery);
                documentClasses.add(PlaylistDocument.class);
            }
            if (type.contains(SearchQuery.Type.TRACK)) {
                Query trackQuery = query.getQuery().trim().isEmpty()
                        ? Query.findAll().setPageable(pageable)
                        : new CriteriaQuery(
                        new Criteria("name").is(query.getQuery()),
                        pageable);
                queries.add(trackQuery);
                documentClasses.add(TrackDocument.class);
            }
        }

        List<SearchHits<?>> hits = elasticsearchOperations.multiSearch(queries, documentClasses);

        PaginationResponse<AlbumResponse> albums = null;
        PaginationResponse<ArtistResponse> artists = null;
        PaginationResponse<PlaylistResponse> playlists = null;
        PaginationResponse<TrackResponse> tracks = null;
        if (type != null && type.contains(SearchQuery.Type.ALBUM)) {
            albums = PaginationResponse.fromSearchHits(
                    hits.getFirst(),
                    (hit) -> AlbumResponse.fromElasticsearchDocument((AlbumDocument) hit, imageUrlMapper),
                    query.getLimit(),
                    query.getOffset(),
                    externalAppConfig.domain() + "/search?query=" + query.getQuery() + "&offset=" + (query.getOffset() + query.getLimit()) + "&limit=" + query.getLimit(),
                    externalAppConfig.domain() + "/search?query=" + query.getQuery() + "&offset=" + (query.getOffset() - query.getLimit()) + "&limit=" + query.getLimit()
            );
            hits.removeFirst();
        }
        if (type != null && type.contains(SearchQuery.Type.ARTIST)) {
            artists = PaginationResponse.fromSearchHits(
                    hits.getFirst(),
                    (hit) -> ArtistResponse.fromElasticsearchDocument((ArtistDocument) hit),
                    query.getLimit(),
                    query.getOffset(),
                    externalAppConfig.domain() + "/search?query=" + query.getQuery() + "&offset=" + (query.getOffset() + query.getLimit()) + "&limit=" + query.getLimit(),
                    externalAppConfig.domain() + "/search?query=" + query.getQuery() + "&offset=" + (query.getOffset() - query.getLimit()) + "&limit=" + query.getLimit()
            );
            hits.removeFirst();
        }
        if (type != null && type.contains(SearchQuery.Type.PLAYLIST)) {
            playlists = PaginationResponse.fromSearchHits(
                    hits.getFirst(),
                    (hit) -> PlaylistResponse.fromElasticsearchDocument((PlaylistDocument) hit, imageUrlMapper),
                    query.getLimit(),
                    query.getOffset(),
                    externalAppConfig.domain() + "/search?query=" + query.getQuery() + "&offset=" + (query.getOffset() + query.getLimit()) + "&limit=" + query.getLimit(),
                    externalAppConfig.domain() + "/search?query=" + query.getQuery() + "&offset=" + (query.getOffset() - query.getLimit()) + "&limit=" + query.getLimit()
            );
            hits.removeFirst();
        }
        if (type != null && type.contains(SearchQuery.Type.TRACK)) {
            tracks = PaginationResponse.fromSearchHits(
                    hits.getFirst(),
                    (hit) -> TrackResponse.fromElasticsearchDocument((TrackDocument) hit),
                    query.getLimit(),
                    query.getOffset(),
                    externalAppConfig.domain() + "/search?query=" + query.getQuery() + "&offset=" + (query.getOffset() + query.getLimit()) + "&limit=" + query.getLimit(),
                    externalAppConfig.domain() + "/search?query=" + query.getQuery() + "&offset=" + (query.getOffset() - query.getLimit()) + "&limit=" + query.getLimit()
            );
            hits.removeFirst();
        }
        return SearchResponse.builder()
                .albums(albums)
                .artists(artists)
                .playlists(playlists)
                .tracks(tracks)
                .build();
    }
}
