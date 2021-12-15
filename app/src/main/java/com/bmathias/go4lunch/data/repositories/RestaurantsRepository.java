package com.bmathias.go4lunch.data.repositories;


import com.bmathias.go4lunch.BuildConfig;
import com.bmathias.go4lunch.data.model.Restaurant;
import com.bmathias.go4lunch.network.model.RestaurantPlaceDetailsResponse.DetailsResultAPI;
import com.bmathias.go4lunch.network.model.RestaurantPlaceResponse.RestaurantAPI;
import com.bmathias.go4lunch.network.PlaceAPIService;
import com.bmathias.go4lunch.network.model.RestaurantPlaceResponse.ResultsAPI;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RestaurantsRepository {

    private final PlaceAPIService apiService;

    // place/nearbysearch/json?location=43.80812051168388,4.638306531512217&radius=1000&type=restaurant&key=AIzaSyDKVEFMvGvHtXCQeWzF1_xYjVDHLuikiCE


    @Inject
    public RestaurantsRepository(PlaceAPIService apiService) {
        this.apiService = apiService;
    }

    String location = "43.80812051168388,4.638306531512217";
    String radius = "1000";
    String type = "restaurant";

    public Observable<List<Restaurant>> streamFetchRestaurants(){
        //PlaceAPIService placeAPIService = PlaceAPIService.retrofit.create(PlaceAPIService.class);

        return apiService.getRestaurantsResult(location, radius, type, BuildConfig.MAPS_API_KEY)
                .map(resultsAPI -> restaurantsConverter(resultsAPI.getResults()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public Observable<DetailsResultAPI> streamFetchRestaurantDetails(String placeId, String api_key) {
        //PlaceAPIService placeAPIService = PlaceAPIService.retrofit.create(PlaceAPIService.class);

        return apiService.getRestaurantDetails(placeId, api_key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    private static List<Restaurant> restaurantsConverter(List<RestaurantAPI> restaurantAPIS) {
        List<Restaurant> restaurants = new ArrayList<>(restaurantAPIS.size());
        for (RestaurantAPI restaurantAPI : restaurantAPIS) {
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
