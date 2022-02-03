package com.bmathias.go4lunch_.data.network;

import com.bmathias.go4lunch_.data.network.model.places.RestaurantsApiResult;

import io.reactivex.Observable;
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
           @Query("key") String api_key
   );

   public static final Retrofit retrofit = new Retrofit.Builder()
           .baseUrl("https://maps.googleapis.com/maps/api/")
           .addConverterFactory(GsonConverterFactory.create())
           .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
           .build();
}
