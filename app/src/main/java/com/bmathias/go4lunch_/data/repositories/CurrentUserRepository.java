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

public class CurrentUserRepository {
   private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
   private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
   private final CollectionReference usersRef = rootRef.collection(USERS);

   private static volatile CurrentUserRepository instance;

   public static CurrentUserRepository getInstance() {
      CurrentUserRepository result = instance;
      if (result != null) {
         return result;
      }
      synchronized (CurrentUserRepository.class) {
         if (instance == null) {
            instance = new CurrentUserRepository();
         }
         return instance;
      }
   }

   public LiveData<User> getCurrentUser() {
      MutableLiveData<User> authenticatedUserInFirebaseLiveData = new MutableLiveData<>();
      FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
      assert firebaseUser != null;
      getUserFromDatabase(firebaseUser.getUid(), authenticatedUserInFirebaseLiveData);
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
            logErrorMessage(userTask.getException().getMessage());
         }
      });
   }
}
