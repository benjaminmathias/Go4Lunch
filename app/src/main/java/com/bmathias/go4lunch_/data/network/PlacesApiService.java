package com.bmathias.go4lunch_.data.network;

import com.bmathias.go4lunch_.data.network.model.places.RestaurantsApiResult;
import com.bmathias.go4lunch_.data.network.model.placesDetails.DetailsResultAPI;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApiService {

    @GET("place/nearbysearch/json")
    Observable<RestaurantsApiResult> getRestaurants(
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("type") String type,
            @Query("key") String key
    );

    @GET("place/details/json")
    Observable<DetailsResultAPI> getRestaurantDetails(
            @Query("place_id") String place_id,
            @Query("key") String key
    );



    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build();

}
