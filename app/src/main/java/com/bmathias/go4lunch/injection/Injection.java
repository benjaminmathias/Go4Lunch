package com.bmathias.go4lunch.injection;

import com.bmathias.go4lunch.BuildConfig;
import com.bmathias.go4lunch.data.network.PlacesApiService;
import com.bmathias.go4lunch.data.repositories.AuthRepository;
import com.bmathias.go4lunch.data.repositories.ChatRepository;
import com.bmathias.go4lunch.data.repositories.ConfigRepository;
import com.bmathias.go4lunch.data.repositories.CurrentUserRepository;
import com.bmathias.go4lunch.data.repositories.DefaultConfigRepository;
import com.bmathias.go4lunch.data.repositories.SharedPrefs;
import com.bmathias.go4lunch.data.repositories.FirestoreUserDatasource;
import com.bmathias.go4lunch.data.repositories.RestaurantRepository;
import com.bmathias.go4lunch.data.repositories.UserDatasource;
import com.bmathias.go4lunch.data.repositories.UsersRepository;
import com.bmathias.go4lunch.utils.App;
import com.bmathias.go4lunch.utils.LocationService;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Injection {

    public static ViewModelFactory provideViewModelFactory() {
        return new ViewModelFactory(
                provideRestaurantRepository(),
                provideAuthRepository(),
                provideCurrentUserRepository(),
                provideUserRepository(),
                provideConfigRepository());
    }

    public static ChatViewModelFactory provideChatViewModelFactory(String userId) {
        return new ChatViewModelFactory(provideChatRepository(), userId);
    }

    public static UserDatasource provideUserDatasource() {
        return new FirestoreUserDatasource(
                provideCurrentUserRepository(),
                FirebaseFirestore.getInstance()
        );
    }

    public static RestaurantRepository provideRestaurantRepository() {
        return RestaurantRepository.getInstance(
                provideLocationService(),
                provideApiService(),
                BuildConfig.PHOTO_BASE_URL,
                provideConfigRepository(),
                FirebaseFirestore.getInstance(),
                provideCurrentUserRepository(),
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                provideUserDatasource());
    }

    private static PlacesApiService provideApiService() {
        return PlacesApiService.retrofit.create(PlacesApiService.class);
    }

    public static LocationService provideLocationService() {
        return LocationService.getInstance(App.getContext());
    }

    public static CurrentUserRepository provideCurrentUserRepository() {
        return CurrentUserRepository.getInstance(FirebaseFirestore.getInstance());
    }

    public static AuthRepository provideAuthRepository() {
        return AuthRepository.getInstance();
    }
    public static UsersRepository provideUserRepository() {
        return UsersRepository.getInstance(FirebaseFirestore.getInstance());
    }

    public static ChatRepository provideChatRepository() {
        return ChatRepository.getInstance(FirebaseFirestore.getInstance(), provideCurrentUserRepository());
    }

    public static SharedPrefs provideSharedPrefs() {
        return SharedPrefs.getInstance();
    }

    public static ConfigRepository provideConfigRepository() {
        return new DefaultConfigRepository(provideSharedPrefs());
    }
}
