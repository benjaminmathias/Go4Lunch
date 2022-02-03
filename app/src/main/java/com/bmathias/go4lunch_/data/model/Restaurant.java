package com.bmathias.go4lunch_.data.model;

import com.bmathias.go4lunch_.data.network.model.places.Location;
import com.bmathias.go4lunch_.data.network.model.places.OpeningHours;

public class Restaurant {

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
        private Restaurant restaurant;
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

        public Restaurant build() {
            Restaurant restaurant = new Restaurant();

            if (name != null) {
                restaurant.setName(name);
            }

            if (address != null) {
                restaurant.setAddress(address);
            }

            if (location != null){
                restaurant.setLocation(location);
            }

            if (isOpen != null){
                restaurant.setIsOpen(isOpen);
            }

            if (placeId != null){
                restaurant.setPlaceId(placeId);
            }

            if (photo != null){
                restaurant.setPhoto(photo);
            }

            return restaurant;
        }
    }
}
