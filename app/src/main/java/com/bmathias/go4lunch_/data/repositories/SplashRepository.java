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

@SuppressWarnings("ConstantConditions")
public class SplashRepository {
   private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
   //private User user = new User();
   private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
   private CollectionReference usersRef = rootRef.collection(USERS);

   private static volatile SplashRepository instance;

   public static SplashRepository getInstance() {
      SplashRepository result = instance;
      if (result != null) {
         return result;
      }
      synchronized (SplashRepository.class) {
         if (instance == null) {
            instance = new SplashRepository();
         }
         return instance;
      }
   }

   public LiveData<User> checkIfUserIsAuthenticatedInFirebase() {
      MutableLiveData<User> authenticatedUserInFirebaseLiveData = new MutableLiveData<>();
      FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
      if (firebaseUser == null) {
         authenticatedUserInFirebaseLiveData.setValue(null);
      } else {
         getUserFromDatabase(firebaseUser.getUid(), authenticatedUserInFirebaseLiveData);
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
            logErrorMessage(userTask.getException().getMessage());
         }
      });
   }

   public MutableLiveData<User> addUserToLiveData(String userId) {
      MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
      usersRef.document(userId).get().addOnCompleteListener(userTask -> {
         if (userTask.isSuccessful()) {
            DocumentSnapshot document = userTask.getResult();
            if(document.exists()) {
               User user = document.toObject(User.class);
               userMutableLiveData.setValue(user);
            }
         } else {
            logErrorMessage(userTask.getException().getMessage());
         }
      });
      return userMutableLiveData;
   }
}
