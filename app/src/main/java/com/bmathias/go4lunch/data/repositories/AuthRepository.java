package com.bmathias.go4lunch.data.repositories;

import static com.bmathias.go4lunch.utils.Constants.USERS;
import static com.bmathias.go4lunch.utils.HelperClass.logErrorMessage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch.data.model.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AuthRepository {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = rootRef.collection(USERS);

    private static volatile AuthRepository instance;

    public static AuthRepository getInstance() {
        AuthRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (AuthRepository.class) {
            if (instance == null) {
                instance = new AuthRepository();
            }
            return instance;
        }
    }

    public LiveData<User> firebaseSignIn(AuthCredential authCredential) {
        MutableLiveData<User> authenticatedUserMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();
                    String userName = firebaseUser.getDisplayName();
                    String userEmail = firebaseUser.getEmail();
                    String photoUrl = Objects.requireNonNull(firebaseUser.getPhotoUrl()).toString();
                    User user = new User(userId, userName, userEmail, photoUrl, null, null);

                    createUserInFirestoreIfNotExists(user, authenticatedUserMutableLiveData);
                    authenticatedUserMutableLiveData.setValue(user);
                }
            } else {
                authenticatedUserMutableLiveData.setValue(null);
                logErrorMessage(Objects.requireNonNull(authTask.getException()).getMessage());
            }
        });
        return authenticatedUserMutableLiveData;
    }

    private void createUserInFirestoreIfNotExists(User authenticatedUser, MutableLiveData<User> userLiveData) {
        DocumentReference uidRef = usersRef.document(authenticatedUser.getUserId());
        uidRef.get().addOnCompleteListener(uidTask -> {
            if (uidTask.isSuccessful()) {
                DocumentSnapshot document = uidTask.getResult();
                if (!document.exists()) {
                    uidRef.set(authenticatedUser).addOnCompleteListener(userCreationTask -> {
                        if (userCreationTask.isSuccessful()) {
                            authenticatedUser.isCreated = true;
                            userLiveData.setValue(authenticatedUser);
                        } else {
                            logErrorMessage(Objects.requireNonNull(userCreationTask.getException()).getMessage());
                            userLiveData.setValue(null);
                        }
                    });
                } else {
                    userLiveData.setValue(authenticatedUser);
                }
            } else {
                logErrorMessage(Objects.requireNonNull(uidTask.getException()).getMessage());
            }
        });
    }
}
