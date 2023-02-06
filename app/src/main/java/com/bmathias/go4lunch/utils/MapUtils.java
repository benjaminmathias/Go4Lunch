package com.bmathias.go4lunch.utils;

import android.location.Location;
import android.util.Log;

public class MapUtils {

    private MapUtils() {}

    public static float getDistance(Double latA, Double lngA, Double latB, Double lngB) {
        Location locationA = new Location("point A");

        locationA.setLatitude(latA);
        locationA.setLongitude(lngA);

        Location locationB = new Location("point B");

        locationB.setLatitude(latB);
        locationB.setLongitude(lngB);

        Log.d("RestaurantRepository", "Distance = " + Math.round(locationA.distanceTo(locationB)));
        return Math.round(locationA.distanceTo(locationB));
    }
}
