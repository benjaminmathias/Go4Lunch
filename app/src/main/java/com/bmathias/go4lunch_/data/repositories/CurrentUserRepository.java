package com.bmathias.go4lunch_.data.repositories;

import static com.bmathias.go4lunch_.utils.Constants.USERS;
import static com.bmathias.go4lunch_.utils.HelperClass.logErrorMessage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch_.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class CurrentUserRepository {
   private static volatile CurrentUserRepository instance;
   private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
   private final CollectionReference usersRef;
   private CurrentUserRepository(FirebaseFirestore firebaseFirestore) {
      usersRef = firebaseFirestore.collection(USERS);
   }
   public static CurrentUserRepository getInstance(FirebaseFirestore firebaseFirestore) {
      CurrentUserRepository result = instance;
      if (result != null) {
         return result;
      }
      synchronized (CurrentUserRepository.class) {
         if (instance == null) {
            instance = new CurrentUserRepository(firebaseFirestore);
         }
         return instance;
      }
   }

   public String getCurrentUserId() {
      FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
      if (firebaseUser != null) {
         return firebaseUser.getUid();
      }
      return null;
   }
   public LiveData<User> getCurrentUser() {
      MutableLiveData<User> authenticatedUserInFirebaseLiveData = new MutableLiveData<>();
      String currentUserId = getCurrentUserId();
      if (currentUserId == null) {
         authenticatedUserInFirebaseLiveData.setValue(null);
      } else {
         getUserFromDatabase(currentUserId, authenticatedUserInFirebaseLiveData);
      }
      return authenticatedUserInFirebaseLiveData;
   }
   private void getUserFromDatabase(String userId, MutableLiveData<User> authenticatedUserInFirebaseLiveData) {
      usersRef.document(userId).get().addOnCompleteListener(userTask -> {
         if (userTask.isSuccessful()) {
            DocumentSnapshot document = userTask.getResult();
            if(document.exists()) {
               User user = document.toObject(User.class);
               authenticatedUserInFirebaseLiveData.setValue(user);
            }
         } else {
            logErrorMessage(Objects.requireNonNull(userTask.getException()).getMessage());
         }
      });
   }
}
