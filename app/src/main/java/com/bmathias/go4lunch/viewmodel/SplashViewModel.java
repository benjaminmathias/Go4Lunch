package com.bmathias.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch.data.model.User;
import com.bmathias.go4lunch.data.repositories.CurrentUserRepository;

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
