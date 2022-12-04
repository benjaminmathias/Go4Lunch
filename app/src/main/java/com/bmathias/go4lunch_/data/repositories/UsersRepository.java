package com.bmathias.go4lunch_.data.repositories;

import static com.bmathias.go4lunch_.utils.Constants.TAG;
import static com.bmathias.go4lunch_.utils.Constants.USERS;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.data.network.model.DataResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Objects;

public final class UsersRepository {

    private static volatile UsersRepository instance;

    private final CollectionReference usersRef;

    private UsersRepository(FirebaseFirestore firebaseFirestore) {
        usersRef = firebaseFirestore.collection(USERS);
    }

    public static UsersRepository getInstance(FirebaseFirestore firebaseFirestore) {
        UsersRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UsersRepository.class) {
            if (instance == null) {
                instance = new UsersRepository(firebaseFirestore);
            }
            return instance;
        }
    }

    // Retrieve all workmates
    public LiveData<List<User>> getUsers() {

        MutableLiveData<List<User>> _users = new MutableLiveData<>();

        usersRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            List<User> users = task.getResult().toObjects(User.class);
                            _users.postValue(users);
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        return _users;
    }

    public LiveData<DataResult<List<User>>> getDataUsers() {

        MutableLiveData<DataResult<List<User>>> _users = new MutableLiveData<>();

        usersRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            List<User> users = task.getResult().toObjects(User.class);
                            DataResult<List<User>> dataResult = new DataResult<>(users);
                            _users.postValue(dataResult);
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Exception exception = task.getException();
                        DataResult<List<User>> dataResult = new DataResult<>(exception);
                        Log.e("onError", "======================================================");
                        Log.e(TAG, "Error getting documents: ", task.getException());
                        Objects.requireNonNull(exception).printStackTrace();
                        Log.e("onError", "======================================================");
                        _users.postValue(dataResult);
                    }
                });

        return _users;
    }

    // Retrieve only users eating at said restaurant in realtime
    public LiveData<List<User>> getUsersByPlaceId(String placeId) {

        MutableLiveData<List<User>> _specificUsers = new MutableLiveData<>();

        usersRef.whereEqualTo("selectedRestaurantId", placeId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.d(TAG, "Listen failed.", error);
                        return;
                    }
                    List<User> users = Objects.requireNonNull(value).toObjects(User.class);
                    _specificUsers.postValue(users);
                });

        return _specificUsers;
    }
}

