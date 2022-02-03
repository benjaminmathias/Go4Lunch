package com.bmathias.go4lunch_.injection;

import android.content.Context;

import com.bmathias.go4lunch_.data.network.PlacesApiService;
import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;

public class Injection {

    public static RestaurantRepository provideRestaurantRepository(){
        PlacesApiService placesAPIService = PlacesApiService.retrofit.create(PlacesApiService.class);
        return RestaurantRepository.getInstance(placesAPIService);
    }


    /*
    private static RestaurantRepository provideRestaurantRepository(Context context) {
        return RestaurantRepository.getInstance(context);
    }*/

    public static ViewModelFactory provideViewModelFactory() {
        return new ViewModelFactory(provideRestaurantRepository());
    }

}
