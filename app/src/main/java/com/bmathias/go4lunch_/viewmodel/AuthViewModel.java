package com.bmathias.go4lunch_.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.data.repositories.AuthRepository;
import com.google.firebase.auth.AuthCredential;

public class AuthViewModel extends ViewModel {
    private final AuthRepository mAuthRepository;
    public LiveData<User> authenticatedUserLiveData;

    public AuthViewModel(AuthRepository authRepository) {
        this.mAuthRepository = authRepository;
    }

    public void signWithAuthCredential(AuthCredential authCredential) {
        authenticatedUserLiveData = mAuthRepository.firebaseSignIn(authCredential);
    }
}
