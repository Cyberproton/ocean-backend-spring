package me.cyberproton.ocean.features.artist;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.profile.dto.ProfileResponse;
import me.cyberproton.ocean.features.profile.util.ProfileMapper;
import me.cyberproton.ocean.features.user.UserDocument;
import me.cyberproton.ocean.util.PersistenceUtils;

import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ArtistMapper {
    private final ProfileMapper profileMapper;

    public ProfileResponse entityToResponse(ArtistEntity artistEntity) {
        if (!PersistenceUtils.isLoaded(artistEntity.getUser())) {
            return null;
        }
        if (!PersistenceUtils.isLoaded(artistEntity.getUser().getProfile())) {
            return null;
        }
        return profileMapper.entityToResponse(artistEntity.getUser().getProfile());
    }

    public ProfileResponse documentToResponse(UserDocument artistDocument) {
        return profileMapper.documentToResponse(artistDocument);
    }
}
