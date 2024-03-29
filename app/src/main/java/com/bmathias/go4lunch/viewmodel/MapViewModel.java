package com.bmathias.go4lunch.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch.data.model.RestaurantItem;
import com.bmathias.go4lunch.data.model.UserLocation;
import com.bmathias.go4lunch.data.repositories.RestaurantRepository;
import com.bmathias.go4lunch.injection.Injection;
import com.bmathias.go4lunch.utils.LocationService;

import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MapViewModel extends ViewModel {

    private static final String TAG = "MapViewModel :";

    private final RestaurantRepository restaurantRepository;

    private final LocationService locationService = Injection.provideLocationService();

    private final MutableLiveData<Boolean> _showProgress = new MutableLiveData<>();
    public LiveData<Boolean> showProgress = _showProgress;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    private Disposable disposable;

    public MutableLiveData<UserLocation> userLocationLiveData = new MutableLiveData<>();

    public MapViewModel(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
        observeLocation();
    }

    // RESTAURANTS
    public LiveData<List<RestaurantItem>> getRestaurants(String query) {
        return Transformations.map(restaurantRepository.getRestaurantsObservable(query), result -> {

            if (result.isSuccess()) {
                Log.e(TAG, "success");
                _error.postValue(null);
                return result.getData();
            } else {
                _error.postValue(result.getError().getMessage());
                Log.e(TAG, result.getError().getMessage());
                return Collections.emptyList();
            }
        });
    }

    // LOCATION
    public void observeLocation() {
        disposable = locationService.retrieveLocation()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(userLocation ->
                        userLocationLiveData.postValue(userLocation));
    }

    public LiveData<UserLocation> getUserLocation() {
        return userLocationLiveData;
    }

    @Override
    protected void onCleared() {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onCleared();
    }
}
