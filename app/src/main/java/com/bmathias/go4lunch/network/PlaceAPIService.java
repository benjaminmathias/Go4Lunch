package com.bmathias.go4lunch.network;

import com.bmathias.go4lunch.BuildConfig;
import com.bmathias.go4lunch.network.model.RestaurantPlaceDetailsResponse.DetailsResultAPI;
import com.bmathias.go4lunch.network.model.RestaurantPlaceResponse.ResultsAPI;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceAPIService {

    // private static final String BASE_URL_SEARCH = "https://maps.googleapis.com/maps/api/place/nearbysearch/";
    // location=43.80812051168388,4.638306531512217
    // key = &key=AIzaSyDKVEFMvGvHtXCQeWzF1_xYjVDHLuikiCE

    // place/nearbysearch/json?location=43.80812051168388,4.638306531512217&radius=1000&type=restaurant&key=AIzaSyDKVEFMvGvHtXCQeWzF1_xYjVDHLuikiCE

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