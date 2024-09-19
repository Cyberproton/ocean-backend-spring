package me.cyberproton.ocean.features.track.util;

import me.cyberproton.ocean.features.track.entity.TrackAnalyticsEntity;

public class TrackUtils {
    /**
     * Calculate popularity based on the number of plays, likes, and timestamps of likes and plays.
     *
     * @param trackAnalyticsEntity Track document
     * @return Popularity
     */
    public static long calculatePopularity(TrackAnalyticsEntity trackAnalyticsEntity) {
        long numberOfPlays =
                trackAnalyticsEntity.getNumberOfPlays() == null
                        ? 0
                        : trackAnalyticsEntity.getNumberOfPlays();
        long numberOfLikes =
                trackAnalyticsEntity.getNumberOfLikes() == null
                        ? 0
                        : trackAnalyticsEntity.getNumberOfLikes();
        long likeTotalTimestampInMinutes =
                trackAnalyticsEntity.getLikeTotalTimestampInMinutes() == null
                        ? 0
                        : trackAnalyticsEntity.getLikeTotalTimestampInMinutes();
        long playTotalTimestampInMinutes =
                trackAnalyticsEntity.getPlayTotalTimestampInMinutes() == null
                        ? 0
                        : trackAnalyticsEntity.getPlayTotalTimestampInMinutes();
        return numberOfPlays
                + numberOfLikes * 5
                + likeTotalTimestampInMinutes / 1440
                + playTotalTimestampInMinutes / 1440;
    }
}
