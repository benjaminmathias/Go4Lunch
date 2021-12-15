package com.bmathias.go4lunch.network;

import com.bmathias.go4lunch.network.model.RestaurantPlaceDetailsResponse.DetailsResultAPI;
import com.bmathias.go4lunch.network.model.RestaurantPlaceResponse.ResultsAPI;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceAPIService {

    @GET("place/nearbysearch/json")
    Observable<ResultsAPI> getRestaurantsResult(
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("type") String type,
            @Query("key") String api_key
    );

    @GET("place/details/json")
    Observable<DetailsResultAPI> getRestaurantDetails(
            @Query("place_id") String place_id,
            @Query("key") String api_key
    );

}