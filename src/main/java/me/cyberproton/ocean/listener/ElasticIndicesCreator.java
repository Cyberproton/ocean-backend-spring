package me.cyberproton.ocean.listener;

import jakarta.annotation.Nonnull;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.album.entity.AlbumDocument;
import me.cyberproton.ocean.features.artist.ArtistDocument;
import me.cyberproton.ocean.features.playlist.entity.PlaylistDocument;
import me.cyberproton.ocean.features.track.entity.TrackDocument;
import me.cyberproton.ocean.features.user.UserDocument;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ElasticIndicesCreator implements ApplicationListener<ContextRefreshedEvent> {
    private static boolean alreadySetup = false;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public void onApplicationEvent(@Nonnull ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        createDefaultIndices();
        alreadySetup = true;
    }

    public void createDefaultIndices() {
        createIndicesIfNotExists(
                AlbumDocument.class,
                ArtistDocument.class,
                PlaylistDocument.class,
                TrackDocument.class,
                UserDocument.class);
    }

    public void deleteDefaultIndices() {
        deleteIndicesIfExists(
                AlbumDocument.class,
                ArtistDocument.class,
                PlaylistDocument.class,
                TrackDocument.class,
                UserDocument.class);
    }

    private void createIndexIfNotExists(Class<?> documentClass) {
        IndexOperations ops = elasticsearchOperations.indexOps(documentClass);
        if (!ops.exists()) {
            ops.create();
        }
    }

    private void createIndicesIfNotExists(Class<?>... documentClasses) {
        for (Class<?> documentClass : documentClasses) {
            createIndexIfNotExists(documentClass);
        }
    }

    private void deleteIndexIfExists(Class<?> documentClass) {
        IndexOperations ops = elasticsearchOperations.indexOps(documentClass);
        if (ops.exists()) {
            ops.delete();
        }
    }

    private void deleteIndicesIfExists(Class<?>... documentClasses) {
        for (Class<?> documentClass : documentClasses) {
            deleteIndexIfExists(documentClass);
        }
    }
}
