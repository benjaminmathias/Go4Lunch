package com.bmathias.go4lunch_.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.data.repositories.SplashRepository;

public class SplashViewModel extends ViewModel {
    private final SplashRepository splashRepository;
    public LiveData<User> isUserAuthenticatedLiveData;

    public SplashViewModel(SplashRepository splashRepository) {
        this.splashRepository = splashRepository;
    }

    public void checkIfUserIsAuthenticated() {
        isUserAuthenticatedLiveData = splashRepository.checkIfUserIsAuthenticatedInFirebase();
    }
}
