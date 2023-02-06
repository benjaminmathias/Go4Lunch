package com.bmathias.go4lunch.data.repositories;

public interface ConfigRepository {

    String getRadius();

    void setRadius(String radius);

    Boolean getNotificationsPreferences();

    void setNotificationsPreferences(Boolean value);
}
