package com.bmathias.go4lunch_.injection;

import android.content.Context;

import com.bmathias.go4lunch_.BuildConfig;
import com.bmathias.go4lunch_.data.network.PlacesApiService;
import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;
import com.bmathias.go4lunch_.data.repositories.UserRepository;

public class Injection {

    public static RestaurantRepository provideRestaurantRepository() {
        return RestaurantRepository.getInstance(provideApiService(), BuildConfig.PHOTO_BASE_URL);
    }

    private static PlacesApiService provideApiService() {
        return PlacesApiService.retrofit.create(PlacesApiService.class);
    }

    public static ViewModelFactory provideViewModelFactory() {
        return new ViewModelFactory(provideRestaurantRepository());
    }

    public static UserRepository provideUserRepository() {
        return UserRepository.getInstance();
    }

    public static UserViewModelFactory provideUserViewModelFactory() {
        return new UserViewModelFactory(provideUserRepository());
    }

}
