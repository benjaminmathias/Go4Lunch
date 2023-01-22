package com.bmathias.go4lunch_.viewmodel;

import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.data.repositories.ConfigRepository;

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
}
