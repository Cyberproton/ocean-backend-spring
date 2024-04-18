package me.cyberproton.ocean.features.artist;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.cyberproton.ocean.features.profile.ProfileDocument;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Document(indexName = "artist")
public class ArtistDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Nested)
    private ProfileDocument profile;
}
