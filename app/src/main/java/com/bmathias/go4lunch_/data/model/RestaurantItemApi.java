package com.bmathias.go4lunch_.data.model;

import com.bmathias.go4lunch_.data.network.model.places.OpeningHours;

import java.util.Objects;

public class RestaurantItemApi {

    private String name;

    private String address;

    private Double longitude;

    private Double latitude;

    private OpeningHours isOpen;

    private String placeId;

    private String photo;

    private float distance;

    private int numberOfPeopleEating;

    private int numberOfFavorites;

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

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getNumberOfPeopleEating() {
        return numberOfPeopleEating;
    }

    public void setNumberOfPeopleEating(int someoneEating) {
        numberOfPeopleEating = someoneEating;
    }

    public int getNumberOfFavorites() {
        return numberOfFavorites;
    }

    public void setNumberOfFavorites(int numberOfFavorites) {
        this.numberOfFavorites = numberOfFavorites;
    }

    public static class Builder {
        private String name;
        private String address;
        private Double longitude;
        private Double latitude;
        private OpeningHours isOpen;
        private String placeId;
        private String photo;
        private float distance;
        private int isSomeoneEating;
        private int numberOfFavorites;

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

        public Builder withDistance(float distance){
            this.distance = distance;
            return this;
        }

        public Builder withIsSomeoneEating(int isSomeoneEating) {
            this.isSomeoneEating = isSomeoneEating;
            return this;
        }

        public Builder withNumberOfFavorites(int numberOfFavorites) {
            this.numberOfFavorites = numberOfFavorites;
            return this;
        }

        public RestaurantItemApi build() {
            RestaurantItemApi restaurantItemApi = new RestaurantItemApi();

            if (name != null) {
                restaurantItemApi.setName(name);
            }

            if (address != null) {
                restaurantItemApi.setAddress(address);
            }

            if (Objects.nonNull(longitude)){
                restaurantItemApi.setLongitude(longitude);
            }

            if (Objects.nonNull(latitude)){
                restaurantItemApi.setLatitude(latitude);
            }

            if (isOpen != null){
                restaurantItemApi.setIsOpen(isOpen);
            }

            if (placeId != null){
                restaurantItemApi.setPlaceId(placeId);
            }

            if (photo != null){
                restaurantItemApi.setPhoto(photo);
            }

            if (Objects.nonNull(distance)){
                restaurantItemApi.setDistance(distance);
            }

            if (Objects.nonNull(isSomeoneEating)){
                restaurantItemApi.setNumberOfPeopleEating(isSomeoneEating);
            }

            if (Objects.nonNull(numberOfFavorites)){
                restaurantItemApi.setNumberOfFavorites(numberOfFavorites);
            }

            return restaurantItemApi;
        }
    }
}
