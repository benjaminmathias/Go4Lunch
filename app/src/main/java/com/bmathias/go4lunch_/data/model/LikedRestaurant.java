package com.bmathias.go4lunch_.data.model;

public class LikedRestaurant {

   private String likedPlaceId;

   private String likedUserId;

   public LikedRestaurant(String likedPlaceId, String likedUserId) {
      this.likedPlaceId = likedPlaceId;
      this.likedUserId = likedUserId;
   }

   public String getLikedPlaceId() {
      return likedPlaceId;
   }

   public void setLikedPlaceId(String likedPlaceId) {
      this.likedPlaceId = likedPlaceId;
   }

   public String getLikedUserId() {
      return likedUserId;
   }

   public void setLikedUserId(String likedUserId) {
      this.likedUserId = likedUserId;
   }
}
