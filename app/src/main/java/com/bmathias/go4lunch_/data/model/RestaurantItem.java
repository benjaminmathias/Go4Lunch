package com.bmathias.go4lunch_.data.model;

import com.bmathias.go4lunch_.data.network.model.places.Location;
import com.bmathias.go4lunch_.data.network.model.places.OpeningHours;

public class RestaurantItem {

    private String name;

    private String address;

    private Location location;

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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
        private Location location;
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

        public Builder withLocation(Location location) {
            this.location = location;
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

            if (location != null){
                restaurantItem.setLocation(location);
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
