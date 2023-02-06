package com.bmathias.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch.data.model.User;
import com.bmathias.go4lunch.data.repositories.CurrentUserRepository;

public class MainViewModel extends ViewModel {
    private final CurrentUserRepository currentUserRepository;
    public LiveData<User> currentUser;

    public MainViewModel(CurrentUserRepository currentUserRepository) {
        this.currentUserRepository = currentUserRepository;
    }

    public void getUserFromDatabase(){
        currentUser = currentUserRepository.getCurrentUser();
    }
}
