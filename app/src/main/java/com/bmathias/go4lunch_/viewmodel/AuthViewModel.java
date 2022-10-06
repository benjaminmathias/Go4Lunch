package com.bmathias.go4lunch_.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.data.repositories.AuthRepository;
import com.google.firebase.auth.AuthCredential;

public class AuthViewModel extends ViewModel {
    private final AuthRepository authRepository;
    public LiveData<User> authenticatedUserLiveData;
    public LiveData<User> createdUserLiveData;

    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void signInWithGoogle(AuthCredential googleAuthCredential) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(googleAuthCredential);
    }
}
