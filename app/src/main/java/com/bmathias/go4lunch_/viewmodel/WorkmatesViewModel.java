package com.bmathias.go4lunch_.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.data.repositories.UsersRepository;

import java.util.List;

public class WorkmatesViewModel extends ViewModel {
    private final UsersRepository usersRepository;

    private LiveData<List<User>> users;

    public WorkmatesViewModel(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
        getUsersFromDatabase();
    }

    public void getUsersFromDatabase() {
        LiveData<List<User>> _users = usersRepository.getUsers();
        users = _users;
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }
}
