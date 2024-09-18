package me.cyberproton.ocean.features.history.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HistoryType {
    // Record new track history if the track is played after 1 minute
    TRACK(false, 60000L),
    ALBUM(true, -1L),
    ARTIST(true, -1L),
    PLAYLIST(true, -1L),
    ;

    /** If true, override the old record with the new one. */
    private final boolean overrideOldRecord;

    /** If overrideOldRecord is false, create a new record after the given timestamp. */
    private final Long createNewRecordAfterTimestamp;
}
