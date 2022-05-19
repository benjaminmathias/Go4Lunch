package com.bmathias.go4lunch_.data.repositories;

import static com.bmathias.go4lunch_.utils.Constants.TAG;
import static com.bmathias.go4lunch_.utils.Constants.USERS;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch_.BuildConfig;
import com.bmathias.go4lunch_.data.model.RestaurantDetails;
import com.bmathias.go4lunch_.data.model.RestaurantItem;
import com.bmathias.go4lunch_.data.network.PlacesApiService;
import com.bmathias.go4lunch_.data.network.model.DataResult;
import com.bmathias.go4lunch_.data.network.model.places.RestaurantApi;
import com.bmathias.go4lunch_.data.network.model.placesDetails.RestaurantDetailsAPI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RestaurantRepository {

    private final String photoBaseUrl;
    private final PlacesApiService placesAPIService;

    private static volatile RestaurantRepository instance;

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

    private static final String location = "43.80812051168388,4.638306531512217";
    private static final String radius = "1000";
    private static final String type = "restaurant";

    private RestaurantRepository(PlacesApiService placesAPIService, String photoBaseUrl) {
        this.placesAPIService = placesAPIService;
        this.photoBaseUrl = photoBaseUrl;
    }

    public static RestaurantRepository getInstance(PlacesApiService placesAPIService, String photoBaseUrl) {
        RestaurantRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (RestaurantRepository.class) {
            if (instance == null) {
                instance = new RestaurantRepository(placesAPIService, photoBaseUrl);
            }
            return instance;
        }
    }

    // Retrieve Restaurant and convert to our own model
    public LiveData<DataResult<List<RestaurantItem>>> streamFetchRestaurants() {
        MutableLiveData<DataResult<List<RestaurantItem>>> _restaurants = new MutableLiveData<>();

        placesAPIService.getRestaurants(location, radius, type, BuildConfig.MAPS_API_KEY)
                .subscribeOn(Schedulers.io())
                .map(restaurantsApiResult -> restaurantsConverter(restaurantsApiResult.getResults(), photoBaseUrl))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurantsItems -> {
                    DataResult<List<RestaurantItem>> dataResult = new DataResult<>(restaurantsItems);
                    _restaurants.postValue(dataResult);
                }, throwable -> {
                    DataResult<List<RestaurantItem>> dataResult = new DataResult<>(throwable);
                    Log.e("onError", "onError : " + throwable.getMessage());
                    throwable.printStackTrace();
                    _restaurants.postValue(dataResult);
                });


        return _restaurants;
    }

    // Retrieve RestaurantDetails and convert to our own model
    public LiveData<DataResult<RestaurantDetails>> streamFetchRestaurantDetails(String placeId) {
        MutableLiveData<DataResult<RestaurantDetails>> _restaurantDetails = new MutableLiveData<>();

        placesAPIService.getRestaurantDetails(placeId, BuildConfig.MAPS_API_KEY)
                .subscribeOn(Schedulers.io())
                .map(detailsResult -> restaurantDetailsConverter(detailsResult.getResult(), photoBaseUrl))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(detailsResult -> {
                    DataResult<RestaurantDetails> dataResult = new DataResult<>(detailsResult);
                    _restaurantDetails.postValue(dataResult);
                }, throwable -> {
                    DataResult<RestaurantDetails> dataResult = new DataResult<>(throwable);
                    _restaurantDetails.postValue(dataResult);
                });

        return _restaurantDetails;
    }

    // Convert the Restaurant model provided by Places API to our own model
    private static List<RestaurantItem> restaurantsConverter(List<RestaurantApi> restaurantApis, String photoBaseUrl) {
        List<RestaurantItem> restaurantItems = new ArrayList<>(restaurantApis.size());
        for (RestaurantApi restaurantAPI : restaurantApis) {
            RestaurantItem.Builder builder = new RestaurantItem.Builder()
                    .withName(restaurantAPI.getName())
                    .withAddress(restaurantAPI.getVicinity())
                    .withIsOpen(restaurantAPI.getOpeningHours())
                    .withPlaceId(restaurantAPI.getPlaceId());

            if (restaurantAPI.getGeometry() != null) {
                builder.withLongitude(restaurantAPI.getGeometry().getLocation().getLng());
                builder.withLatitude(restaurantAPI.getGeometry().getLocation().getLat());
            }

            if (restaurantAPI.getPhotos() != null && !restaurantAPI.getPhotos().isEmpty()) {
                String photoReference = restaurantAPI.getPhotos().get(0).getPhotoReference();
                String photoUrl = photoBaseUrl + photoReference + "&key=" + BuildConfig.MAPS_API_KEY;
                builder.withPhoto(photoUrl);
            }

            RestaurantItem restaurantItem = builder.build();
            restaurantItems.add(restaurantItem);

        }
        return restaurantItems;
    }

    // Convert the RestaurantDetails model provided by Places Details API to our own model
    private static RestaurantDetails restaurantDetailsConverter(RestaurantDetailsAPI restaurantDetailsAPI, String photoBaseUrl) {
        RestaurantDetails.Builder builder = new RestaurantDetails.Builder()
                .withAddress(restaurantDetailsAPI.getFormattedAddress());

        if (restaurantDetailsAPI.getName() != null) {
            builder.withName(restaurantDetailsAPI.getName());
        }

        if (restaurantDetailsAPI.getPlaceId() != null) {
            builder.withPlaceId(restaurantDetailsAPI.getPlaceId());
        }
        if (restaurantDetailsAPI.getInternationalPhoneNumber() != null) {
            builder.withPhoneNumber(restaurantDetailsAPI.getFormattedPhoneNumber());
        }

        if (restaurantDetailsAPI.getWebsite() != null) {
            builder.withWebsite(restaurantDetailsAPI.getWebsite());
        }

        if (restaurantDetailsAPI.getPhotos() != null && !restaurantDetailsAPI.getPhotos().isEmpty()) {
            String photoReference = restaurantDetailsAPI.getPhotos().get(0).getPhotoReference();
            String photoUrl = photoBaseUrl + photoReference + "&key=" + BuildConfig.MAPS_API_KEY;
            builder.withPhotoUrl(photoUrl);
        }

        return builder.build();
    }


    // Update user selected restaurant
    public void updateSelectedRestaurant(String placeId, String placeName) {

        if (firebaseAuth.getUid() != null) {
            DocumentReference userRef = rootRef.collection(USERS).document(firebaseAuth.getUid());
            userRef.update("selectedRestaurantId", placeId).addOnSuccessListener(aVoid -> Log.d(TAG, "selectedRestaurantId successfully updated!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
            userRef.update("selectedRestaurantName", placeName).addOnSuccessListener(aVoid -> Log.d(TAG, "selectedRestaurantName successfully updated!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
        }
    }

    // Delete user selected restaurant
    public void deleteSelectedRestaurant() {

        if (firebaseAuth.getUid() != null) {
            DocumentReference userRef = rootRef.collection(USERS).document(firebaseAuth.getUid());
            userRef.update("selectedRestaurantId", null).addOnSuccessListener(aVoid -> Log.d(TAG, "selectedRestaurantId successfully deleted!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
            userRef.update("selectedRestaurantName", null).addOnSuccessListener(aVoid -> Log.d(TAG, "selectedRestaurantName successfully deleted!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
        }
    }

}
