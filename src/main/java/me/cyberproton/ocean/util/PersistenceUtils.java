package me.cyberproton.ocean.util;

import jakarta.persistence.Persistence;

public class PersistenceUtils {
    public static boolean isLoaded(Object object) {
        return Persistence.getPersistenceUtil().isLoaded(object);
    }
}
