package com.bmathias.go4lunch_.data.repositories;

import static com.bmathias.go4lunch_.data.network.PlacesApiService.retrofit;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch_.BuildConfig;
import com.bmathias.go4lunch_.data.model.Restaurant;
import com.bmathias.go4lunch_.data.network.PlacesApiService;
import com.bmathias.go4lunch_.data.network.model.DataResult;
import com.bmathias.go4lunch_.data.network.model.places.RestaurantApi;
import com.bmathias.go4lunch_.data.network.model.places.RestaurantsApiResult;
import com.bmathias.go4lunch_.data.network.model.placesDetails.DetailsResultAPI;
import com.bmathias.go4lunch_.data.network.model.placesDetails.RestaurantDetailsAPI;
import com.bmathias.go4lunch_.ui.list.DetailsActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class RestaurantRepository {

    private final PlacesApiService placesAPIService;

    private static volatile RestaurantRepository INSTANCE;

    private static String location = "43.80812051168388,4.638306531512217";
    private static final String radius = "1000";
    private static final String type = "restaurant";

    public static RestaurantRepository getInstance(PlacesApiService placesAPIService) {
        return INSTANCE == null ? new RestaurantRepository(placesAPIService) : INSTANCE;
    }

    public RestaurantRepository(PlacesApiService placesAPIService) {
        this.placesAPIService = placesAPIService;
    }

    public LiveData<DataResult<List<RestaurantApi>>> streamFetchRestaurants() {
        MutableLiveData<DataResult<List<RestaurantApi>>> _restaurants = new MutableLiveData<>();

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

    public LiveData<DataResult<RestaurantDetailsAPI>> streamFetchRestaurantDetails(String placeId) {
        MutableLiveData<DataResult<RestaurantDetailsAPI>> _restaurantDetails = new MutableLiveData<>();

        placesAPIService.getRestaurantDetails(placeId, BuildConfig.MAPS_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DetailsResultAPI>() {
                    @Override
                    public void accept(DetailsResultAPI detailsResultAPI) throws Exception {
                        DataResult<RestaurantDetailsAPI> dataResult = new DataResult<>(detailsResultAPI.getResult());
                        _restaurantDetails.postValue(dataResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        DataResult<RestaurantDetailsAPI> dataResult = new DataResult<>(throwable);
                        _restaurantDetails.postValue(dataResult);
                    }
                });

        return _restaurantDetails;
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
