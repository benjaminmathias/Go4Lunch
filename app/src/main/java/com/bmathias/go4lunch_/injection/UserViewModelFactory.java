package com.bmathias.go4lunch_.injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.bmathias.go4lunch_.data.repositories.UserRepository;
import com.bmathias.go4lunch_.data.manager.UserManager;

public class UserViewModelFactory implements ViewModelProvider.Factory {

    private final UserRepository userDatasource;

    public UserViewModelFactory(UserRepository userDatasource) {
        this.userDatasource = userDatasource;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        if (aClass.isAssignableFrom(UserManager.class)) {
        //   return (T) new UserManager(userDatasource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
