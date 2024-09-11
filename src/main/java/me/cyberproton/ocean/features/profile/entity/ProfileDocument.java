package me.cyberproton.ocean.features.profile.entity;

import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import me.cyberproton.ocean.features.file.FileDocument;

import org.springframework.data.elasticsearch.annotations.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Document(indexName = "profile")
public class ProfileDocument {
    @Id private Long id;

    // Taken from UserEntity.username
    private String username;

    private String name;

    private String bio;

    private FileDocument avatar;

    private FileDocument banner;
}
