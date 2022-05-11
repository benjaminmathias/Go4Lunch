package com.bmathias.go4lunch_.data.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class User implements Serializable {

    private String userId;

    private String userName;

    private String photoUrl;

    @SuppressWarnings("WeakerAccess")
    private String userEmail;

    private String selectedRestaurantId;

    private String selectedRestaurantName;

    @Exclude
    public boolean isAuthenticated;

    @Exclude
    public boolean isNew, isCreated;


    public User() {
    }

    public User(String userId, String userName, String userEmail, String photoUrl, String selectedRestaurantId, String selectedRestaurantName) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.photoUrl = photoUrl;
        this.selectedRestaurantId = selectedRestaurantId;
        this.selectedRestaurantName = selectedRestaurantName;
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

}
