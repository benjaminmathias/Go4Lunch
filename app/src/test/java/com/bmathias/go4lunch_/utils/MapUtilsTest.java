package com.bmathias.go4lunch_.utils;

import static org.junit.Assert.assertEquals;

import android.location.Location;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MapUtilsTest {

    private static final double DELTA = 1e-15;
    Location location;

    @Test
    public void testGetDistance1() {
        float distance = MapUtils.getDistance(43.81831737540004, 4.611814125628988, 43.81647812546067, 4.618702914697175);
        assertEquals(589.13, distance, DELTA);
    }
}