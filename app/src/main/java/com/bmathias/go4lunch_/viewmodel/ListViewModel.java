package com.bmathias.go4lunch_.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.data.model.RestaurantItem;
import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;

import java.util.List;

public class ListViewModel extends ViewModel {

    private static final String TAG = "ListViewModel";

    private final RestaurantRepository restaurantRepository;

    private final MutableLiveData<Boolean> _showProgress = new MutableLiveData<>();
    public LiveData<Boolean> showProgress = _showProgress;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    private LiveData<List<RestaurantItem>> restaurants;

    public ListViewModel(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
        observeRestaurants();
    }


    public void observeRestaurants(){
        _showProgress.postValue(true);
        LiveData<List<RestaurantItem>> _restaurant = Transformations.map(restaurantRepository.streamFetchRestaurants(), result -> {
            _showProgress.postValue(false);

            if (result.isSuccess()) {
                return result.getData();
            } else {
                _error.postValue(result.getError().getMessage());
                return null;
            }
        });

        restaurants = _restaurant;
    }

    public LiveData<List<RestaurantItem>> getRestaurants() {
        return restaurants;
    }

}
