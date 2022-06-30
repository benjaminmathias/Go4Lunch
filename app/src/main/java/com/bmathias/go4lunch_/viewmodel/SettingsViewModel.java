package com.bmathias.go4lunch_.viewmodel;

import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.data.repositories.MySharedPrefs;

public class SettingsViewModel extends ViewModel {

    private final MySharedPrefs sharedPrefs;

    public SettingsViewModel(MySharedPrefs sharedPreferencesDatasource) {
        this.sharedPrefs = sharedPreferencesDatasource;
    }

    public void writeSharedPreferences(String key, String value){
        sharedPrefs.putString(key, value);
    }


    public String readSharedPreferences(String key, String value){
       return sharedPrefs.getString(key, value);

    }
}
