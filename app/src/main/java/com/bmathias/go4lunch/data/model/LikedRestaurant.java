package com.bmathias.go4lunch.data.model;

public class LikedRestaurant {

   private String restaurantId;

   private String userId;

   @SuppressWarnings("unused")
   public LikedRestaurant(){
   }

   @SuppressWarnings("unused")
   public LikedRestaurant(String restaurantId, String userId) {
      this.restaurantId = restaurantId;
      this.userId = userId;
   }

   public String getRestaurantId() {
      return restaurantId;
   }

   @SuppressWarnings("unused")
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
