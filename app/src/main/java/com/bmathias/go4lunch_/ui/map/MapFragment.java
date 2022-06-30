package com.bmathias.go4lunch_.ui.map;

import static com.bmathias.go4lunch_.utils.Constants.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.data.model.UserLocation;
import com.bmathias.go4lunch_.databinding.FragmentMapBinding;
import com.bmathias.go4lunch_.injection.Injection;
import com.bmathias.go4lunch_.injection.ViewModelFactory;
import com.bmathias.go4lunch_.ui.list.DetailsActivity;
import com.bmathias.go4lunch_.viewmodel.MapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Marker mapMarker;
    private MapViewModel mapViewModel;

    private FragmentMapBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        this.setupViewModel();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapView mapView = binding.map;

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        mapView.getMapAsync(this);
    }

    private void setupViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        this.mapViewModel = new ViewModelProvider(this, viewModelFactory).get(MapViewModel.class);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        mapViewModel.getUserLocation().observe(getViewLifecycleOwner(), this::setupMap);



      /*  locationDisposable = this.mapViewModel.getUserLocation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setupMap);*/
    }


    /**
     * @MissingPermission used here, we check the location permissions on another activity at runtime
     */
    @SuppressLint("MissingPermission")
    private void setupMap(UserLocation userLocation) {

        Log.d(TAG, "setupMap" + userLocation.getLongitude() + "," + userLocation.getLatitude());

        googleMap.setMyLocationEnabled(true);

        mapViewModel.getRestaurants().observe(getViewLifecycleOwner(), restaurantItems -> {
            // Add markers for each restaurant with a tag
            for (int i = 0; i < restaurantItems.size(); i++) {
                if (!restaurantItems.get(i).getIsSomeoneEating()) {
                    mapMarker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(restaurantItems.get(i).getLatitude(), restaurantItems.get(i).getLongitude()))
                            .title(restaurantItems.get(i).getName())
                            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_location))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                } else {
                    mapMarker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(restaurantItems.get(i).getLatitude(), restaurantItems.get(i).getLongitude()))
                            .title(restaurantItems.get(i).getName())
                            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_location))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
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


        if (this.mapViewModel.getUserLocation() != null) {
            double latitude = userLocation.getLatitude();
            double longitude = userLocation.getLongitude();

            Log.d("MapFragment", "Lat : " + latitude + " ; Lng : " + longitude);

            // Animate map to phone location
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18.0f));
        }

    }

    // Method used to covert vector asset to bitmap (used for map marker)
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        Objects.requireNonNull(vectorDrawable).setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}

