package com.bmathias.go4lunch_.data.model;

public class LikedRestaurant {

   private String restaurantId;

   private String userId;

   public LikedRestaurant(){
   }

   public LikedRestaurant(String restaurantId, String userId) {
      this.restaurantId = restaurantId;
      this.userId = userId;
   }

   public String getRestaurantId() {
      return restaurantId;
   }

   public void setRestaurantId(String restaurantId) {
      this.restaurantId = restaurantId;
   }

   public String getUserId() {
      return userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }
}
