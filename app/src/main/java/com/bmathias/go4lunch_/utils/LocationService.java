package com.bmathias.go4lunch_.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.bmathias.go4lunch_.data.model.UserLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class LocationService {

    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final BehaviorSubject<UserLocation> latestLocation = BehaviorSubject.create();
    private boolean hasSettingUp = false;

    private static volatile LocationService instance;

    public Context context;

    public LocationService(Context context) {
        this.context = context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public static LocationService getInstance(Context context) {
        LocationService result = instance;
        if (result != null) {
            return result;
        }
        synchronized (LocationService.class) {
            if (instance == null) {
                instance = new LocationService(context);
            }
            return instance;
        }
    }

    @SuppressLint("MissingPermission")
    public Observable<UserLocation> retrieveLocation() {

        Log.d("LocationService", "hasSettingUp value :" + hasSettingUp);
        if (!hasSettingUp) {
            setupLocation();
            hasSettingUp = true;
        }
        return latestLocation;
    }

    @SuppressLint("MissingPermission")
    private void setupLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            if (location != null) {
                try {
                    // Get phone location
                    Geocoder geocoder = new Geocoder(fusedLocationProviderClient.getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    double latitude = addresses.get(0).getLatitude();
                    double longitude = addresses.get(0).getLongitude();

                    // Valeur du publish
                    Log.d("LocationHelper", latitude + "," + longitude);

                    UserLocation userLocation = new UserLocation(latitude, longitude);

                    latestLocation.onNext(userLocation);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
