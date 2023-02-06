package com.bmathias.go4lunch.data.repositories;

public class DefaultConfigRepository implements ConfigRepository {

    private final SharedPrefs sharedPrefs;

    public DefaultConfigRepository(SharedPrefs sharedPrefs) {
        this.sharedPrefs = sharedPrefs;
    }

    @Override
    public String getRadius() {
        return sharedPrefs.getRadius();
    }

    @Override
    public void setRadius(String radius) {
        sharedPrefs.setRadius(radius);
    }

    @Override
    public Boolean getNotificationsPreferences(){
        return sharedPrefs.getNotificationsPreferences();
    }

    @Override
    public void setNotificationsPreferences(Boolean value){
        sharedPrefs.setNotificationsPreferences(value);
    }
}
