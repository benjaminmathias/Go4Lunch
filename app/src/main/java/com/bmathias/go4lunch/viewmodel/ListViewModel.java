package com.bmathias.go4lunch.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch.data.model.RestaurantItem;
import com.bmathias.go4lunch.data.repositories.RestaurantRepository;

import java.util.Collections;
import java.util.List;

public class ListViewModel extends ViewModel {

    private static final String TAG = "ListViewModel :";

    private final RestaurantRepository restaurantRepository;

    private final MutableLiveData<Boolean> _showProgress = new MutableLiveData<>();
    public LiveData<Boolean> showProgress = _showProgress;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    public ListViewModel(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public LiveData<List<RestaurantItem>> getRestaurants(String query){
        _showProgress.postValue(true);
        return Transformations.map(restaurantRepository.getRestaurantsObservable(query), result -> {
            _showProgress.postValue(false);

            if (result.isSuccess()) {
                Log.e(TAG, "success");
                _error.postValue(null);
                return result.getData();
            } else {
                Log.e(TAG, result.getError().getMessage());
                _error.postValue(result.getError().getMessage());
                return Collections.emptyList();
            }
        });
    }
}
