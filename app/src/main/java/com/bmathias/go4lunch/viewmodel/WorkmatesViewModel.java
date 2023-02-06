package com.bmathias.go4lunch.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch.data.model.User;
import com.bmathias.go4lunch.data.repositories.CurrentUserRepository;
import com.bmathias.go4lunch.data.repositories.UsersRepository;

import java.util.Collections;
import java.util.List;

public class WorkmatesViewModel extends ViewModel {

    private static final String TAG = "WorkmatesViewModel :";

    private final UsersRepository usersRepository;

    private final CurrentUserRepository currentUserRepository;

    private final MutableLiveData<Boolean> _showProgress = new MutableLiveData<>();
    public LiveData<Boolean> showProgress = _showProgress;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    public WorkmatesViewModel(UsersRepository usersRepository, CurrentUserRepository currentUserRepository) {
        this.usersRepository = usersRepository;
        this.currentUserRepository = currentUserRepository;
    }

    public String getCurrentUserId() {
        return currentUserRepository.getCurrentUserId();
    }

    public LiveData<List<User>> getUsers() {
        _showProgress.postValue(true);
        return Transformations.map(usersRepository.getDataUsers(), result -> {
            _showProgress.postValue(false);

            if (result.isSuccess()) {
                Log.e(TAG, "success");
                return result.getData();
            } else {
                Log.e(TAG, result.getError().getMessage());
                _error.postValue(result.getError().getMessage());
                return Collections.emptyList();
            }
        });
    }
}
