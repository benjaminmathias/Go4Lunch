package com.bmathias.go4lunch.viewmodel;

import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch.data.repositories.ConfigRepository;

public class SettingsViewModel extends ViewModel {

    private final ConfigRepository configRepository;

    public SettingsViewModel(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public void writeRadius(String value){
        configRepository.setRadius(value);
    }

    public String readRadiusValue() {
        return configRepository.getRadius();
    }

    public void setNotificationPreference(Boolean value){
        configRepository.setNotificationsPreferences(value);
    }

    public Boolean readNotificationPreferenceValue(){
        return configRepository.getNotificationsPreferences();
    }
}
