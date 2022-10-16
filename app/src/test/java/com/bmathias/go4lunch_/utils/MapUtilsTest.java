package com.bmathias.go4lunch_.utils;

import static org.junit.Assert.assertEquals;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MapUtilsTest {

    private static final double DELTA = 1e-15;

     private final double FAKE_LATA = 43.87015407479068;
     private final double FAKE_LONA = 4.588206329521588;
     private final double FAKE_LATB = 43.870084463674374;
     private final double FAKE_LONB = 4.588280090119231;

    @Mock
    private Location mockLocationA;
    @Mock
    private Location mockLocationB;

    @Test
    public void testGetDistance1() {
        // When coordinates of 2 points are ...
        Mockito.when(mockLocationA.getLatitude()).thenReturn(FAKE_LATA);
        Mockito.when(mockLocationA.getLongitude()).thenReturn(FAKE_LONA);
        Mockito.when(mockLocationB.getLatitude()).thenReturn(FAKE_LATB);
        Mockito.when(mockLocationB.getLongitude()).thenReturn(FAKE_LONB);

        // Compute distance
        float distance = MapUtils.getDistance(mockLocationA.getLatitude(), mockLocationA.getLongitude(),
                mockLocationB.getLatitude(), mockLocationB.getLongitude());

        /*float distanceMock = MapUtils.getDistance(FAKE_LATA,FAKE_LONA,
                FAKE_LATB, FAKE_LONB);*/

        // Expected distance of 9.5 meters
        float expectedDistance = 9.5f;

        assertEquals(expectedDistance, distance, DELTA);
    }

    @Test
    public void testGetDistance2() {
        // When coordinates of 2 points are ...
        Mockito.when(mockLocationA.getLatitude()).thenReturn(FAKE_LATA);
        Mockito.when(mockLocationA.getLongitude()).thenReturn(FAKE_LONA);
        Mockito.when(mockLocationB.getLatitude()).thenReturn(FAKE_LATB);
        Mockito.when(mockLocationB.getLongitude()).thenReturn(FAKE_LONB);

       /* mockLocationA.setLatitude(FAKE_LATA);
        mockLocationA.setLongitude(FAKE_LONA);
        mockLocationB.setLatitude(FAKE_LATB);
        mockLocationB.setLongitude(FAKE_LONB);*/

        // Compute distance
        float distance2 = MapUtils.getDistance2(mockLocationA, mockLocationB);

        // Expected distance of 9.5 meters
        float expectedDistance = 9.5f;

        assertEquals(expectedDistance, distance2, DELTA);
    }
}