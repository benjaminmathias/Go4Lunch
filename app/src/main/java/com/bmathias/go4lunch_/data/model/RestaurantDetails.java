package com.bmathias.go4lunch_.data.model;

public class RestaurantDetails {

    private String name;

    private String placeId;

    private String address;

    private String phoneNumber;

    private String website;

    private String photoUrl;

    private boolean isCurrentUserFavorite;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
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

    public boolean getCurrentUserFavorite() {
        return isCurrentUserFavorite;
    }

    public void setCurrentUserFavorite(boolean currentUserFavorite) {
        isCurrentUserFavorite = currentUserFavorite;
    }

    public static class Builder {
        private String name;
        private String placeId;
        private String address;
        private String phoneNumber;
        private String website;
        private String photoUrl;
        private Boolean isCurrentUserFavorite;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPlaceId(String placeId) {
            this.placeId = placeId;
            return this;
        }

        public Builder withAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder withPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder withWebsite(String website) {
            this.website = website;
            return this;
        }

        public Builder withPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public Builder withIsCurrentUserFavorite(boolean isCurrentUserFavorite) {
            this.isCurrentUserFavorite = isCurrentUserFavorite;
            return this;
        }

        public RestaurantDetails build() {
            RestaurantDetails restaurantDetails = new RestaurantDetails();

            if (name != null) {
                restaurantDetails.setName(name);
            }

            if (placeId != null) {
                restaurantDetails.setPlaceId(placeId);
            }
            if (address != null) {
                restaurantDetails.setAddress(address);
            }

            if (phoneNumber != null) {
                restaurantDetails.setPhoneNumber(phoneNumber);
            }

            if (website != null) {
                restaurantDetails.setWebsite(website);
            }

            if (photoUrl != null) {
                restaurantDetails.setPhotoUrl(photoUrl);
            }

            if (isCurrentUserFavorite != null) {
                restaurantDetails.setCurrentUserFavorite(isCurrentUserFavorite);
            }

            return restaurantDetails;
        }
    }
}
