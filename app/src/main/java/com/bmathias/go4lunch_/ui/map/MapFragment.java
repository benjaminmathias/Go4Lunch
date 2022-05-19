package com.bmathias.go4lunch_.ui.map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.injection.Injection;
import com.bmathias.go4lunch_.injection.ViewModelFactory;
import com.bmathias.go4lunch_.ui.list.DetailsActivity;
import com.bmathias.go4lunch_.viewmodel.MapViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private Marker mapMarker;


    private MapViewModel mapViewModel;

    FusedLocationProviderClient fusedLocationProviderClient;
    Double latitude;
    Double longitude;
    LatLng userLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        this.setupViewModel();

        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        mapView.getMapAsync(this);

    }

    private void setupViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        this.mapViewModel = new ViewModelProvider(this, viewModelFactory).get(MapViewModel.class);
        this.mapViewModel.observeRestaurants();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        setupMap();
    }

    @SuppressLint("MissingPermission")
    private void setupMap() {
        googleMap.setMyLocationEnabled(true);

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            if (location != null) {
                try {

                    // Get phone location
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    latitude = addresses.get(0).getLatitude();
                    longitude = addresses.get(0).getLongitude();
                    Log.d("MapFragment", "Latitude : " + latitude + ", Longitude: " + longitude);
                    userLocation = new LatLng(latitude, longitude);

                    // Animate map to phone location
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18.0f));

                    // Add restaurants markers near phone location
                    mapViewModel.getRestaurants().observe(getViewLifecycleOwner(), restaurantItems -> {

                        // Add markers for each restaurant with a tag
                        for (int i = 0; i < restaurantItems.size(); i++) {
                            mapMarker = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(restaurantItems.get(i).getLatitude(), restaurantItems.get(i).getLongitude()))
                                    .title(restaurantItems.get(i).getName())
                            );
                            Objects.requireNonNull(mapMarker).setTag(i);
                        }

                        // Handle click on marker by retrieving marker's tag
                        googleMap.setOnMarkerClickListener(marker -> {
                            Log.d("MapFragment", "marker position" + marker.getPosition());
                            int position = (Integer) marker.getTag();

                            Intent intent = new Intent(MapFragment.this.getActivity(), DetailsActivity.class);
                            intent.putExtra("placeId", restaurantItems.get(position).getPlaceId());
                            Log.d("MapFragment", "Clicked on : " + restaurantItems.get(position).getPlaceId());
                            startActivity(intent);

                            return false;
                        });
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

