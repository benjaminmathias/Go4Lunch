package com.bmathias.go4lunch_.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.data.model.RestaurantItem;
import com.bmathias.go4lunch_.data.model.UserLocation;
import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;
import com.bmathias.go4lunch_.injection.Injection;
import com.bmathias.go4lunch_.utils.LocationService;

import java.util.List;

import io.reactivex.Observable;
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

    private LiveData<List<RestaurantItem>> restaurants;


    private Disposable disposable;

    public MutableLiveData<UserLocation> userLocationLiveData;


    public MapViewModel(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
        observeRestaurants();
        observeLocation();
    }

    public void observeRestaurants() {
        //  _showProgress.postValue(true);
        LiveData<List<RestaurantItem>> _restaurant = Transformations.map(restaurantRepository.streamFetchRestaurants(), result -> {
            //  _showProgress.postValue(false);

            if (result.isSuccess()) {
                Log.e(TAG, "success");
                return result.getData();
            } else {
                _error.postValue(result.getError().getMessage());
                Log.e(TAG, result.getError().getMessage());
                return null;
            }
        });
        restaurants = _restaurant;
    }

    public LiveData<List<RestaurantItem>> getRestaurants() {
        return restaurants;
    }

    public void observeLocation() {
        disposable = locationService.retrieveLocation()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(userLocation ->
                        userLocationLiveData.postValue(userLocation));
    }

    public LiveData<UserLocation> getUserLocation(){
        return userLocationLiveData;
    }


    @Override
    protected void onCleared() {
        disposable.isDisposed();
        super.onCleared();
    }
}
