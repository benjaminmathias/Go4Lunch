package com.bmathias.go4lunch_.injection;

import android.content.Context;

import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;

public class Injection {

    private static RestaurantRepository provideRestaurantRepository(Context context) {
        return RestaurantRepository.getInstance(context);
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        return new ViewModelFactory(provideRestaurantRepository(context));
    }

}
