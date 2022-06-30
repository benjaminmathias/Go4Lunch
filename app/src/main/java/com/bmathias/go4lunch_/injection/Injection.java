package com.bmathias.go4lunch_.injection;

import com.bmathias.go4lunch_.BuildConfig;
import com.bmathias.go4lunch_.data.network.PlacesApiService;
import com.bmathias.go4lunch_.data.repositories.AuthRepository;
import com.bmathias.go4lunch_.data.repositories.CurrentUserRepository;
import com.bmathias.go4lunch_.data.repositories.MySharedPrefs;
import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;
import com.bmathias.go4lunch_.data.repositories.SplashRepository;
import com.bmathias.go4lunch_.data.repositories.UsersRepository;
import com.bmathias.go4lunch_.utils.App;
import com.bmathias.go4lunch_.utils.LocationService;

public class Injection {

    public static ViewModelFactory provideViewModelFactory() {
        return new ViewModelFactory(provideRestaurantRepository(), provideAuthRepository(),
                provideCurrentUserRepository(), provideSplashRepository(), provideUserRepository(), provideSharedPrefs());
    }

    public static RestaurantRepository provideRestaurantRepository() {
        return RestaurantRepository.getInstance(provideLocationService(), provideApiService(), BuildConfig.PHOTO_BASE_URL, provideSharedPrefs());
    }

    private static PlacesApiService provideApiService() {
        return PlacesApiService.retrofit.create(PlacesApiService.class);
    }

    public static LocationService provideLocationService(){
        return LocationService.getInstance(App.getContext());
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

    public static MySharedPrefs provideSharedPrefs() {return MySharedPrefs.getInstance();}

}
