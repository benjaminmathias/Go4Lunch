package com.bmathias.go4lunch_.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.data.repositories.CurrentUserRepository;
import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;
import com.google.android.gms.location.FusedLocationProviderClient;

public class MainViewModel extends ViewModel {
    private CurrentUserRepository currentUserRepository;
    private RestaurantRepository restaurantRepository;
    public LiveData<User> currentUser;

    public MainViewModel(CurrentUserRepository currentUserRepository, RestaurantRepository restaurantRepository) {
        this.currentUserRepository = currentUserRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public void getUserFromDatabase(){
        currentUser = currentUserRepository.getCurrentUser();
    }

}
