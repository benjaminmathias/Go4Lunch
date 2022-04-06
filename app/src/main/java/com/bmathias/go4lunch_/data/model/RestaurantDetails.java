package com.bmathias.go4lunch_.data.model;

public class RestaurantDetails {

    private String name;

    private String address;

    private String phoneNumber;

    private String website;

    private String photoUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public static class Builder {
        private String name;
        private String address;
        private String phoneNumber;
        private String website;
        private String photoUrl;

        public Builder withName(String name){
            this.name = name;
            return this;
        }

        public Builder withAddress(String address){
            this.address = address;
            return this;
        }

        public Builder withPhoneNumber(String phoneNumber){
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder withWebsite(String website){
            this.website = website;
            return this;
        }

        public Builder withPhotoUrl(String photoUrl){
            this.photoUrl = photoUrl;
            return this;
        }

        public RestaurantDetails build() {
            RestaurantDetails restaurantDetails = new RestaurantDetails();

            if(name != null){
                restaurantDetails.setName(name);
            }

            if(address != null){
                restaurantDetails.setAddress(address);
            }

            if(phoneNumber != null){
                restaurantDetails.setPhoneNumber(phoneNumber);
            }

            if(website != null){
                restaurantDetails.setWebsite(website);
            }

            if(photoUrl != null){
                restaurantDetails.setPhotoUrl(photoUrl);
            }

            return restaurantDetails;
        }
    }
}
