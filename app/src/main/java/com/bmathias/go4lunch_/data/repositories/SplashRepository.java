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

public class SplashRepository {

   private static volatile SplashRepository instance;

   private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

   private final CollectionReference usersRef;

   private SplashRepository(FirebaseFirestore firebaseFirestore){
      usersRef = firebaseFirestore.collection(USERS);
   }

   public static SplashRepository getInstance(FirebaseFirestore firebaseFirestore) {
      SplashRepository result = instance;
      if (result != null) {
         return result;
      }
      synchronized (SplashRepository.class) {
         if (instance == null) {
            instance = new SplashRepository(firebaseFirestore);
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
         getUser(firebaseUser.getUid(), authenticatedUserInFirebaseLiveData);
      }
      return authenticatedUserInFirebaseLiveData;
   }

   private void getUser(String userId, MutableLiveData<User> authenticatedUserInFirebaseLiveData) {
      usersRef.document(userId).get().addOnCompleteListener(userTask -> {
         if (userTask.isSuccessful()) {
            DocumentSnapshot document = userTask.getResult();
            if(document.exists()) {
               User user = document.toObject(User.class);
               authenticatedUserInFirebaseLiveData.setValue(user);
            } else {
               // TODO : create firebase user
            }
         } else {
            logErrorMessage(Objects.requireNonNull(userTask.getException()).getMessage());
         }
      });
   }
}
