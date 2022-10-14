package com.bmathias.go4lunch_.ui.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.data.model.RestaurantItem;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Marker mapMarker;
    private MapViewModel mapViewModel;

    private FragmentMapBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        requireActivity().setTitle(R.string.list_fragment_name);
        this.setupViewModel();
        observeLiveData();
        loadRestaurants(null);

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.list_search_hint));
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                googleMap.clear();
                loadRestaurants(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Reset the map when the user delete his search
        ImageView clearButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        clearButton.setOnClickListener(v -> {
            if(searchView.getQuery().length() == 0) {
                searchView.setIconified(true);
            } else {
                searchView.setQuery(null, false);
                googleMap.clear();
                loadRestaurants(null);
            }
        });

        menuItem.setVisible(true);
    }

    private void setupViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        this.mapViewModel = new ViewModelProvider(this, viewModelFactory).get(MapViewModel.class);
    }

    private void loadRestaurants(String query){
        this.mapViewModel.loadRestaurants(query).observe(getViewLifecycleOwner(), aBoolean -> {
        });
    }

    private void observeLiveData(){
        mapViewModel.error.observe(getViewLifecycleOwner(), error -> {

        });

        mapViewModel.showProgress.observe(getViewLifecycleOwner(), isVisible -> {

        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        mapViewModel.getUserLocation().observe(getViewLifecycleOwner(), this::setupMap);
    }

    /**
     * @MissingPermission used here, we check the location permissions on another activity at runtime
     */
    @SuppressLint("MissingPermission")
    private void setupMap(UserLocation userLocation) {

        if (this.mapViewModel.getUserLocation() != null) {
            double latitude = userLocation.getLatitude();
            double longitude = userLocation.getLongitude();

            Log.d("MapFragment", "Lat : " + latitude + " ; Lng : " + longitude);

            // Animate map to phone location
            googleMap.setMyLocationEnabled(true);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitude, longitude)).zoom(14).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        mapViewModel.getRestaurants().observe(getViewLifecycleOwner(), restaurantItems -> {
            // Add markers for each restaurant with a tag
            for (RestaurantItem restaurantItem : restaurantItems) {
                if (restaurantItem.getNumberOfPeopleEating() == 0) {
                    mapMarker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(restaurantItem.getLatitude(), restaurantItem.getLongitude()))
                            .title(restaurantItem.getName())
                            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_location))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                } else {
                    mapMarker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(restaurantItem.getLatitude(), restaurantItem.getLongitude()))
                            .title(restaurantItem.getName())
                            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_location))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
                Objects.requireNonNull(mapMarker).setTag(restaurantItem.getPlaceId());
            }

            // Handle click on marker by retrieving marker's tag
            googleMap.setOnMarkerClickListener(marker -> {
                Log.d("MapFragment", "marker position" + marker.getPosition());
                String placeId = (String) marker.getTag();
                Intent intent = new Intent(MapFragment.this.getActivity(), DetailsActivity.class);
                intent.putExtra("placeId", placeId);
                Log.d("MapFragment", "Clicked on : " + placeId);
                startActivity(intent);
                return false;
            });
        });
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

