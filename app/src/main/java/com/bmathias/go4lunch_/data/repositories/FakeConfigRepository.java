package com.bmathias.go4lunch_.data.repositories;

public class FakeConfigRepository implements ConfigRepository {
    @Override
    public String getRadius() {
        return "200";
    }

    @Override
    public void setRadius(String radius) {

    }
}