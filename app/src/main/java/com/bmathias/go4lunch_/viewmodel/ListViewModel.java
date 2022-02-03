package com.bmathias.go4lunch_.viewmodel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.data.network.model.DataResult;
import com.bmathias.go4lunch_.data.network.model.places.RestaurantApi;
import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class ListViewModel extends ViewModel {

    private static final String TAG = "ListViewModel";

    private final RestaurantRepository restaurantRepository;

    private final MutableLiveData<Boolean> _showProgress = new MutableLiveData<>();
    public LiveData<Boolean> showProgress = _showProgress;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    private LiveData<List<RestaurantApi>> restaurants;

    public ListViewModel(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
        observeRestaurants();
    }


    public void observeRestaurants(){
        LiveData<List<RestaurantApi>> _restaurant = Transformations.map(restaurantRepository.streamFetchRestaurants(), new Function<DataResult<List<RestaurantApi>>, List<RestaurantApi>>() {
            @Override
            public List<RestaurantApi> apply(DataResult<List<RestaurantApi>> result) {
                _showProgress.postValue(false);

                if (result.isSuccess()) {
                    return result.getData();
                } else {
                    _error.postValue(result.getError().getMessage());
                    return null;
                }
            }
        });

        restaurants = _restaurant;
    }

    public LiveData<List<RestaurantApi>> getRestaurants() {
        return restaurants;
    }


    private final MutableLiveData<List<RestaurantApi>> _restaurants2 = new MutableLiveData<>();
    public LiveData<List<RestaurantApi>> restaurants2 = _restaurants2;
    private void observeRestaurants2(){
        Transformations.map(restaurantRepository.streamFetchRestaurants(), new Function<DataResult<List<RestaurantApi>>, List<RestaurantApi>>() {
            @Override
            public List<RestaurantApi> apply(DataResult<List<RestaurantApi>> result) {
                _showProgress.postValue(false);

                if (result.isSuccess()) {
                    _restaurants2.postValue(result.getData());
                } else {
                    _error.postValue(result.getError().getMessage());

                }
                return null;
            }
        });
    }

}
