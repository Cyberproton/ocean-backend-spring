package me.cyberproton.ocean.features.history.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import me.cyberproton.ocean.features.history.dto.CreateHistoryRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;

public class IsValidHistoryValidator
        implements ConstraintValidator<IsValidHistory, CreateHistoryRequest> {
    private static final Logger log = LoggerFactory.getLogger(IsValidHistoryValidator.class);

    @Override
    public boolean isValid(
            CreateHistoryRequest createHistoryRequest,
            ConstraintValidatorContext constraintValidatorContext) {
        Long[] ids =
                new Long[] {
                    createHistoryRequest.trackId(),
                    createHistoryRequest.albumId(),
                    createHistoryRequest.artistId(),
                    createHistoryRequest.playlistId()
                };

        log.info("Checking if history is valid: {}", createHistoryRequest);

        // Check if there is only one non-null id
        if (Arrays.stream(ids).filter(Objects::nonNull).count() > 1) {
            return false;
        }

        return switch (createHistoryRequest.type()) {
            case TRACK -> createHistoryRequest.trackId() != null;
            case ALBUM -> createHistoryRequest.albumId() != null;
            case ARTIST -> createHistoryRequest.artistId() != null;
            case PLAYLIST -> createHistoryRequest.playlistId() != null;
        };
    }
}
