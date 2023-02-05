package com.bmathias.go4lunch_.data.mappers;

import static com.bmathias.go4lunch_.utils.Constants.TAG;
import static com.bmathias.go4lunch_.utils.MapUtils.getDistance;

import android.util.Log;

import com.bmathias.go4lunch_.BuildConfig;
import com.bmathias.go4lunch_.data.model.RestaurantDetails;
import com.bmathias.go4lunch_.data.model.RestaurantItem;
import com.bmathias.go4lunch_.data.network.model.places.RestaurantApi;
import com.bmathias.go4lunch_.data.network.model.placesDetails.RestaurantDetailsApiModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestaurantMapper {
    private RestaurantMapper() {
    }

    public static RestaurantDetails apiToDetails(
            RestaurantDetailsApiModel restaurantDetailsApiModel,
            String photoBaseUrl,
            Boolean isCurrentUserFavorite,
            String mapsApiKey
    ) {
        RestaurantDetails.Builder builder = new RestaurantDetails.Builder()
                .withAddress(restaurantDetailsApiModel.getFormattedAddress());

        if (restaurantDetailsApiModel.getName() != null) {
            builder = builder.withName(restaurantDetailsApiModel.getName());
        }

        if (restaurantDetailsApiModel.getPlaceId() != null) {
            builder = builder.withPlaceId(restaurantDetailsApiModel.getPlaceId());
        }
        if (restaurantDetailsApiModel.getInternationalPhoneNumber() != null) {
            builder = builder.withPhoneNumber(restaurantDetailsApiModel.getFormattedPhoneNumber());
        }

        if (restaurantDetailsApiModel.getWebsite() != null) {
            builder = builder.withWebsite(restaurantDetailsApiModel.getWebsite());
        }

        if (restaurantDetailsApiModel.getPhotos() != null && !restaurantDetailsApiModel.getPhotos().isEmpty()) {
            String photoReference = restaurantDetailsApiModel.getPhotos().get(0).getPhotoReference();
            String photoUrl = photoBaseUrl + photoReference + "&key=" + mapsApiKey;
            builder = builder.withPhotoUrl(photoUrl);
        }

        builder = builder.withIsCurrentUserFavorite(isCurrentUserFavorite);

        return builder.build();
    }

    public static List<RestaurantItem> apisToItems(
            List<RestaurantApi> restaurantApis,
            String photoBaseUrl,
            List<String> eatingAtRestaurant,
            List<String> likedRestaurants,
            Double userLatitude,
            Double userLongitude
    ) {
        List<RestaurantItem> restaurantsList = new ArrayList<>(restaurantApis.size());
        for (RestaurantApi restaurantAPI : restaurantApis) {
            RestaurantItem restaurants = apiToItem(restaurantAPI,
                    photoBaseUrl,
                    eatingAtRestaurant,
                    likedRestaurants,
                    userLatitude,
                    userLongitude);
            restaurantsList.add(restaurants);
        }
        return restaurantsList;
    }

    public static RestaurantItem apiToItem(
            RestaurantApi restaurantAPI,
            String photoBaseUrl,
            List<String> eatingAtRestaurant,
            List<String> likedRestaurants,
            Double userLatitude,
            Double userLongitude
    ) {
        boolean isOpened = false;
        if (restaurantAPI.getOpeningHours() != null) {
            isOpened = restaurantAPI.getOpeningHours().getOpenNow();
        }


        RestaurantItem.Builder builder = new RestaurantItem.Builder()
                .withName(restaurantAPI.getName())
                .withAddress(restaurantAPI.getVicinity())
                .withPlaceId(restaurantAPI.getPlaceId())
                .withIsOpen(isOpened);

        if (restaurantAPI.getGeometry() != null) {
            builder = builder.withLongitude(restaurantAPI.getGeometry().getLocation().getLng());
            builder = builder.withLatitude(restaurantAPI.getGeometry().getLocation().getLat());
            builder = builder.withDistance(Math.round(getDistance(
                    restaurantAPI.getGeometry().getLocation().getLat(),
                    restaurantAPI.getGeometry().getLocation().getLng(),
                    userLatitude,
                    userLongitude))
            );
        }
       /*
        if (restaurantAPI.getOpeningHours() != null){
            builder = builder.withIsOpen(restaurantAPI.getOpeningHours().getOpenNow());
        } else {
            builder = builder.withIsOpen(false);
        }*/

        if (restaurantAPI.getPhotos() != null && !restaurantAPI.getPhotos().isEmpty()) {
            String photoReference = restaurantAPI.getPhotos().get(0).getPhotoReference();
            String photoUrl = photoBaseUrl + photoReference + "&key=" + BuildConfig.MAPS_API_KEY;
            builder = builder.withPhoto(photoUrl);
        }

        if (eatingAtRestaurant != null) {
            int numberOfPeopleEating = Collections.frequency(eatingAtRestaurant, restaurantAPI.getPlaceId());
            builder = builder.withIsSomeoneEating(numberOfPeopleEating);
        }

        if (likedRestaurants != null) {
            int numberOfFavorites = Collections.frequency(likedRestaurants, restaurantAPI.getPlaceId());
            builder = builder.withNumberOfFavorites(numberOfFavorites);
            Log.d(TAG, "Number of likes for " + restaurantAPI.getName() + " = " + numberOfFavorites);
        }

        return builder.build();
    }
}
