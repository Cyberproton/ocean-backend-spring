package me.cyberproton.ocean.features.user;

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
@Document(indexName = "user")
public class UserDocument {
    @Id
    private Long id;

    private String username;

    private String email;

    private String password;
}
