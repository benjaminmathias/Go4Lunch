package com.bmathias.go4lunch_.data.repositories;

public class DefaultConfigRepository implements ConfigRepository {

    private final DistanceSharedPrefs sharedPrefs;

    public DefaultConfigRepository(DistanceSharedPrefs sharedPrefs) {
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
}
