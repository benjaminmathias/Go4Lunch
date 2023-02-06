package com.bmathias.go4lunch.data.repositories;

public class FakeConfigRepository implements ConfigRepository {
    @Override
    public String getRadius() {
        return "200";
    }

    @Override
    public void setRadius(String radius) {
    }

    @Override
    public Boolean getNotificationsPreferences() {
        return true;
    }

    @Override
    public void setNotificationsPreferences(Boolean value){
    }
}
