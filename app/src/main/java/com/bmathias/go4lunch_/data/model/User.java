package com.bmathias.go4lunch_.data.model;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

   public String uid;

   public String name;

   public Uri photoUrl;

   @SuppressWarnings("WeakerAccess")
   public String email;

   public String selectedRestaurant;

   public List<String> likedRestaurants;

   @Exclude
   public boolean isAuthenticated;

   @Exclude
   boolean isNew, isCreated;

   public User() {}

   public User(String uid, String name, String email, Uri photoUrl, String selectedRestaurant, List<String> likedRestaurants) {
      this.uid = uid;
      this.name = name;
      this.email = email;
      this.photoUrl = photoUrl;
      this.selectedRestaurant = selectedRestaurant;
      this.likedRestaurants = likedRestaurants;
   }
}
