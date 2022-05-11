package com.bmathias.go4lunch_.injection;

import com.bmathias.go4lunch_.BuildConfig;
import com.bmathias.go4lunch_.data.network.PlacesApiService;
import com.bmathias.go4lunch_.data.repositories.AuthRepository;
import com.bmathias.go4lunch_.data.repositories.CurrentUserRepository;
import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;
import com.bmathias.go4lunch_.data.repositories.SplashRepository;
import com.bmathias.go4lunch_.data.repositories.UsersRepository;

public class Injection {

    public static ViewModelFactory provideViewModelFactory() {
        return new ViewModelFactory(provideRestaurantRepository(), provideAuthRepository(),
                provideCurrentUserRepository(), provideSplashRepository(), provideUserRepository());
    }

    public static RestaurantRepository provideRestaurantRepository() {
        return RestaurantRepository.getInstance(provideApiService(), BuildConfig.PHOTO_BASE_URL);
    }

    private static PlacesApiService provideApiService() {
        return PlacesApiService.retrofit.create(PlacesApiService.class);
    }

    public static CurrentUserRepository provideCurrentUserRepository() {
        return CurrentUserRepository.getInstance();
    }

    public static AuthRepository provideAuthRepository() {
        return AuthRepository.getInstance();
    }

    public static SplashRepository provideSplashRepository() {
        return SplashRepository.getInstance();
    }

    public static UsersRepository provideUserRepository() {
        return UsersRepository.getInstance();
    }

}
