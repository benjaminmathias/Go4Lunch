package com.bmathias.go4lunch_.data.repositories;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch_.BuildConfig;
import com.bmathias.go4lunch_.data.model.RestaurantDetails;
import com.bmathias.go4lunch_.data.model.RestaurantItem;
import com.bmathias.go4lunch_.data.network.PlacesApiService;
import com.bmathias.go4lunch_.data.network.model.DataResult;
import com.bmathias.go4lunch_.data.network.model.places.RestaurantApi;
import com.bmathias.go4lunch_.data.network.model.placesDetails.DetailsResultAPI;
import com.bmathias.go4lunch_.data.network.model.placesDetails.RestaurantDetailsAPI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class RestaurantRepository {

    private final String photoBaseUrl;
    private final PlacesApiService placesAPIService;

    private static volatile RestaurantRepository INSTANCE;

    private static final String location = "43.80812051168388,4.638306531512217";
    private static final String radius = "1000";
    private static final String type = "restaurant";

    public static RestaurantRepository getInstance(PlacesApiService placesAPIService, String photoBaseUrl) {
        return INSTANCE == null ? new RestaurantRepository(placesAPIService, photoBaseUrl) : INSTANCE;
    }

    public RestaurantRepository(PlacesApiService placesAPIService, String photoBaseUrl) {
        this.placesAPIService = placesAPIService;
        this.photoBaseUrl = photoBaseUrl;
    }

    public LiveData<DataResult<List<RestaurantItem>>> streamFetchRestaurants() {
        MutableLiveData<DataResult<List<RestaurantItem>>> _restaurants = new MutableLiveData<>();

        placesAPIService.getRestaurants(location, radius, type, BuildConfig.MAPS_API_KEY)
                .subscribeOn(Schedulers.io())
                .map(restaurantsApiResult -> restaurantsConverter(restaurantsApiResult.getResults(), photoBaseUrl))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<RestaurantItem>>() {
                    @Override
                    public void accept(List<RestaurantItem> restaurantsItems) throws Exception {
                        DataResult<List<RestaurantItem>> dataResult = new DataResult<>(restaurantsItems);
                        _restaurants.postValue(dataResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        DataResult<List<RestaurantItem>> dataResult = new DataResult<>(throwable);
                        _restaurants.postValue(dataResult);
                    }
                });

        return _restaurants;
    }

    public LiveData<DataResult<RestaurantDetails>> streamFetchRestaurantDetails(String placeId) {
        MutableLiveData<DataResult<RestaurantDetails>> _restaurantDetails = new MutableLiveData<>();

        placesAPIService.getRestaurantDetails(placeId, BuildConfig.MAPS_API_KEY)
                .subscribeOn(Schedulers.io())
                .map(detailsResult -> restaurantDetailsConverter(detailsResult.getResult(), photoBaseUrl))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RestaurantDetails>() {
                    @Override
                    public void accept(RestaurantDetails detailsResult) throws Exception {
                        DataResult<RestaurantDetails> dataResult = new DataResult<>(detailsResult);
                        _restaurantDetails.postValue(dataResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        DataResult<RestaurantDetails> dataResult = new DataResult<>(throwable);
                        _restaurantDetails.postValue(dataResult);
                    }
                });

        return _restaurantDetails;
    }

    private static List<RestaurantItem> restaurantsConverter(List<RestaurantApi> restaurantApis, String photoBaseUrl) {
        List<RestaurantItem> restaurantItems = new ArrayList<>(restaurantApis.size());
        for (RestaurantApi restaurantAPI : restaurantApis) {
            RestaurantItem.Builder builder = new RestaurantItem.Builder()
                    .withName(restaurantAPI.getName())
                    .withAddress(restaurantAPI.getVicinity())
                    .withIsOpen(restaurantAPI.getOpeningHours())
                    .withPlaceId(restaurantAPI.getPlaceId());

            if (restaurantAPI.getGeometry() != null) {
                builder.withLocation(restaurantAPI.getGeometry().getLocation());
            }

            if(restaurantAPI.getPhotos() != null && !restaurantAPI.getPhotos().isEmpty()){
                String photoReference = restaurantAPI.getPhotos().get(0).getPhotoReference();
                String photoUrl = photoBaseUrl + photoReference + "&key=" + BuildConfig.MAPS_API_KEY;
                builder.withPhoto(photoUrl);
            }

            RestaurantItem restaurantItem = builder.build();
            restaurantItems.add(restaurantItem);
        }
        return restaurantItems;
    }

    private static RestaurantDetails restaurantDetailsConverter(RestaurantDetailsAPI restaurantDetailsAPI, String photoBaseUrl) {
        RestaurantDetails.Builder builder = new RestaurantDetails.Builder()
                .withAddress(restaurantDetailsAPI.getFormattedAddress());

                if(restaurantDetailsAPI.getName() != null) {
                    builder.withName(restaurantDetailsAPI.getName());
                }

                if(restaurantDetailsAPI.getInternationalPhoneNumber() != null){
                    builder.withPhoneNumber(restaurantDetailsAPI.getFormattedPhoneNumber());
                }

                if(restaurantDetailsAPI.getWebsite() != null){
                    builder.withWebsite(restaurantDetailsAPI.getWebsite());
                }

                if(restaurantDetailsAPI.getPhotos() != null && !restaurantDetailsAPI.getPhotos().isEmpty()){
                    String photoReference = restaurantDetailsAPI.getPhotos().get(0).getPhotoReference();
                    String photoUrl = photoBaseUrl + photoReference + "&key=" + BuildConfig.MAPS_API_KEY;
                    builder.withPhotoUrl(photoUrl);
                }

        return builder.build();

    }

}
