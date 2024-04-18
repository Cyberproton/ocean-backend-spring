package me.cyberproton.ocean.features.profile;

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
@Document(indexName = "profile")
public class ProfileDocument {
    @Id
    private Long id;

    private String name;

    private String bio;

    private Long avatarId;

    private Long bannerId;
}
