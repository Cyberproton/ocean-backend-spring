package me.cyberproton.ocean.features.album;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@AllArgsConstructor
public class AlbumListener {
    private final ElasticsearchOperations elasticsearchOperations;

    @PostPersist
    @PostUpdate
    public void onCreatedOrUpdated(Album album) {
        AlbumDocument albumDocument = AlbumDocument.builder()
                .id(album.getId())
                .type(album.getType())
                .name(album.getName())
                .description(album.getDescription())
                .releaseDate(album.getReleaseDate())
                .coverId(album.getCover().getId())
                .build();

        elasticsearchOperations.save(albumDocument);
    }

    @PostRemove
    public void onDeleted(Album album) {
        elasticsearchOperations.delete(
                album.getId().toString(),
                AlbumDocument.class
        );
    }
}
