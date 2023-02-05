package com.bmathias.go4lunch_.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.data.repositories.CurrentUserRepository;

public class SplashViewModel extends ViewModel {


    private final CurrentUserRepository currentUserRepository;

    public LiveData<User> isUserAuthenticatedLiveData;

    public SplashViewModel(CurrentUserRepository currentUserRepository) {
        this.currentUserRepository = currentUserRepository;
    }

    public void checkIfUserIsAuthenticated() {
        isUserAuthenticatedLiveData = currentUserRepository.getCurrentUser();
    }
}
