package com.bmathias.go4lunch_.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;
/*
public class ListViewModelFactory implements ViewModelProvider.Factory {

    private final RestaurantRepository restaurantRepository;

    public ListViewModelFactory(RestaurantRepository restaurantRepository){
        this.restaurantRepository = restaurantRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        return (T) new ListViewModel(restaurantRepository, executor);
    }
}
*/