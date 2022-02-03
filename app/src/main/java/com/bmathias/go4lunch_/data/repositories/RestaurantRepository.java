package com.bmathias.go4lunch_.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch_.BuildConfig;
import com.bmathias.go4lunch_.data.model.Restaurant;
import com.bmathias.go4lunch_.data.network.PlacesApiService;
import com.bmathias.go4lunch_.data.network.model.DataResult;
import com.bmathias.go4lunch_.data.network.model.places.RestaurantApi;
import com.bmathias.go4lunch_.data.network.model.places.RestaurantsApiResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RestaurantRepository {

    private static final RestaurantRepository instance = new RestaurantRepository();

    private static String location = "43.80812051168388,4.638306531512217";
    private static final String radius = "1000";
    private static final String type = "restaurant";

    public RestaurantRepository() {

    }

    public static RestaurantRepository getInstance(Context context) {
        return instance == null ? new RestaurantRepository() : instance;
    }

    public LiveData<DataResult<List<RestaurantApi>>> streamFetchRestaurants() {
        MutableLiveData<DataResult<List<RestaurantApi>>> _restaurants = new MutableLiveData<>();

        PlacesApiService placesAPIService = PlacesApiService.retrofit.create(PlacesApiService.class);
        placesAPIService.getRestaurants(location, radius, type, BuildConfig.MAPS_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RestaurantsApiResult>() {
                    @Override
                    public void accept(RestaurantsApiResult restaurantsApiResult) throws Exception {
                        DataResult<List<RestaurantApi>> dataResult = new DataResult<>(restaurantsApiResult.getResults());
                        _restaurants.postValue(dataResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        DataResult<List<RestaurantApi>> dataResult = new DataResult<>(throwable);
                        _restaurants.postValue(dataResult);
                    }
                });

        return _restaurants;
    }

    private static List<Restaurant> restaurantsConverter(List<RestaurantApi> restaurantAPIS) {
        List<Restaurant> restaurants = new ArrayList<>(restaurantAPIS.size());
        for (RestaurantApi restaurantAPI : restaurantAPIS) {
            Restaurant restaurant = new Restaurant.Builder()
                    .withName(restaurantAPI.getName())
                    .withAddress(restaurantAPI.getVicinity())
                    .withLocation(restaurantAPI.getGeometry().getLocation())
                    .withIsOpen(restaurantAPI.getOpeningHours())
                    .withPlaceId(restaurantAPI.getPlaceId())
                    .withPhoto(restaurantAPI.getPhotos().get(0).getPhotoReference())
                    .build();
            restaurants.add(restaurant);
        }
        return restaurants;
    }


}
