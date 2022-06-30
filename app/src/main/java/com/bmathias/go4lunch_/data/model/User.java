package com.bmathias.go4lunch_.data.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User implements Serializable {

    private String userId;

    private String userName;

    private String photoUrl;

    @SuppressWarnings("WeakerAccess")
    private String userEmail;

    private String selectedRestaurantId;

    private String selectedRestaurantName;

    private List<String> favoriteRestaurants;

    @Exclude
    public boolean isAuthenticated;

    @Exclude
    public boolean isNew, isCreated;


    public User() {
    }

    public User(String userId, String userName, String userEmail, String photoUrl, String selectedRestaurantId, String selectedRestaurantName, List<String> favoriteRestaurants) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.photoUrl = photoUrl;
        this.selectedRestaurantId = selectedRestaurantId;
        this.selectedRestaurantName = selectedRestaurantName;
        this.favoriteRestaurants = favoriteRestaurants;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getSelectedRestaurantId() {
        return selectedRestaurantId;
    }

    public void setSelectedRestaurantId(String selectedRestaurantId) {
        this.selectedRestaurantId = selectedRestaurantId;
    }

    public String getSelectedRestaurantName() {
        return selectedRestaurantName;
    }

    public void setSelectedRestaurantName(String selectedRestaurantName) {
        this.selectedRestaurantName = selectedRestaurantName;
    }

    public List<String> getFavoriteRestaurants() {
        return favoriteRestaurants;
    }

    public void setFavoriteRestaurants(List<String> favoriteRestaurants) {
        this.favoriteRestaurants = favoriteRestaurants;
    }
}
