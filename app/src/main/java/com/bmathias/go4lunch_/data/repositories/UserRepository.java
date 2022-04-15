package com.bmathias.go4lunch_.data.repositories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public final class UserRepository {

    private static volatile UserRepository instance;

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    public Task<Void> signOut(Context context) {
        LiveData<Boolean> _signedOut = new MutableLiveData<>();
        AuthUI.getInstance().signOut(context);
        return AuthUI.getInstance().signOut(context);
    }

    public LiveData<Boolean> signOut2(Context context) {
        MutableLiveData<Boolean> _signedOut = new MutableLiveData<>();

        AuthUI.getInstance().signOut(context)
                .addOnSuccessListener(unused -> _signedOut.postValue(true))
                .addOnFailureListener(e -> _signedOut.postValue(false));

        return _signedOut;
    }
}
