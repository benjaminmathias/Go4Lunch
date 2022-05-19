package com.bmathias.go4lunch_.data.model;

import com.bmathias.go4lunch_.data.network.model.places.OpeningHours;

import java.util.Objects;

public class RestaurantItem {

    private String name;

    private String address;

    private Double longitude;

    private Double latitude;

    private OpeningHours isOpen;

    private String placeId;

    private String photo;

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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public OpeningHours getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(OpeningHours isOpen) {
        this.isOpen = isOpen;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static class Builder {
        private String name;
        private String address;
        private Double longitude;
        private Double latitude;
        private OpeningHours isOpen;
        private String placeId;
        private String photo;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder withLongitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder withLatitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder withIsOpen(OpeningHours isOpen){
            this.isOpen = isOpen;
            return this;
        }

        public Builder withPlaceId(String placeId){
            this.placeId = placeId;
            return this;
        }

        public Builder withPhoto(String photo){
            this.photo = photo;
            return this;
        }

        public RestaurantItem build() {
            RestaurantItem restaurantItem = new RestaurantItem();

            if (name != null) {
                restaurantItem.setName(name);
            }

            if (address != null) {
                restaurantItem.setAddress(address);
            }

            if (Objects.nonNull(longitude)){
                restaurantItem.setLongitude(longitude);
            }

            if (Objects.nonNull(latitude)){
                restaurantItem.setLatitude(latitude);
            }

            if (isOpen != null){
                restaurantItem.setIsOpen(isOpen);
            }

            if (placeId != null){
                restaurantItem.setPlaceId(placeId);
            }

            if (photo != null){
                restaurantItem.setPhoto(photo);
            }

            return restaurantItem;
        }
    }
}
