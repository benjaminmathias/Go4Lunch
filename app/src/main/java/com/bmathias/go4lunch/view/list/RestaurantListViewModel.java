package com.bmathias.go4lunch.view.list;


import androidx.annotation.NonNull;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch.data.model.Restaurant;
import com.bmathias.go4lunch.data.repositories.RestaurantsRepository;
import com.bmathias.go4lunch.network.model.RestaurantPlaceResponse.RestaurantAPI;
import com.bmathias.go4lunch.network.model.RestaurantPlaceResponse.ResultsAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class RestaurantListViewModel extends ViewModel {

    private static final String TAG = "RestaurantsListViewModel";

    private RestaurantsRepository repository;
    private MutableLiveData<ArrayList<Restaurant>> restaurantList = new MutableLiveData<>();

    @Inject
    public RestaurantListViewModel(RestaurantsRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ArrayList<Restaurant>> getRestaurantList(){
        return restaurantList;
    }

   /* public void getRestaurants(){
        repository.streamFetchRestaurants();
    }*/

    public void getRestaurants(){
        repository.streamFetchRestaurants();

    }

}
