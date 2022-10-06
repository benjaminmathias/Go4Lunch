package com.bmathias.go4lunch_.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.data.model.RestaurantItem;
import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;

import java.util.List;

public class ListViewModel extends ViewModel {

    private static final String TAG = "ListViewModel :";

    private final RestaurantRepository restaurantRepository;

    private final MutableLiveData<Boolean> _showProgress = new MutableLiveData<>();
    public LiveData<Boolean> showProgress = _showProgress;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    private final MutableLiveData<List<RestaurantItem>> restaurants = new MutableLiveData<>();

    public ListViewModel(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public LiveData<Boolean> loadRestaurants(String query) {
        _showProgress.postValue(true);
        return Transformations.map(restaurantRepository.getRestaurantsObservable(query), result -> {
            _showProgress.postValue(false);

            if (result.isSuccess()) {
                Log.e(TAG, "success");
                restaurants.postValue(result.getData());
                return true;
            } else {
                _error.postValue(result.getError().getMessage());
                Log.e(TAG, result.getError().getMessage());
                return false;
            }
        });
    }


    public LiveData<List<RestaurantItem>> getRestaurants() {
        return restaurants;
    }

}
