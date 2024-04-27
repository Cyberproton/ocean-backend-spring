package me.cyberproton.ocean.features.track;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.elasticsearch.TrackDocument;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@AllArgsConstructor
public class TrackListener {
    private final ElasticsearchOperations elasticsearchOperations;

    @PostPersist
    @PostUpdate
    public void onTrackCreatedOrUpdated(Track track) {
        TrackDocument trackDocument = TrackDocument.builder()
                .id(track.getId())
                .name(track.getName())
                .trackNumber(track.getTrackNumber())
                .duration(track.getDuration())
                .fileId(track.getFile().getId())
                .albumId(track.getAlbum().getId())
                .build();
        elasticsearchOperations.save(trackDocument);
    }

    @PostRemove
    public void onTrackDeleted(Track track) {
        elasticsearchOperations.delete(
                track.getId().toString(),
                TrackDocument.class
        );
    }
}
