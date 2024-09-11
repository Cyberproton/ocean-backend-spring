package me.cyberproton.ocean.features.artist;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Document(indexName = "artist")
public class ArtistDocument {
    @Id private Long id;
}
