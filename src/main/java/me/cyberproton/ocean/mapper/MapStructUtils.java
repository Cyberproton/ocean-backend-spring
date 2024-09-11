package me.cyberproton.ocean.mapper;

import jakarta.persistence.Persistence;
import org.mapstruct.Condition;

public class MapStructUtils {
    @Condition
    public static <T> boolean isLazyInitialized(T object) {
        if (object == null) {
            return false;
        }
        return Persistence.getPersistenceUtil().isLoaded(object);
    }
}
