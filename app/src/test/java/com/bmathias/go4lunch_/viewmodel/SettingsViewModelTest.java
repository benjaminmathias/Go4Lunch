package com.bmathias.go4lunch_.viewmodel;

import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.bmathias.go4lunch_.data.repositories.ConfigRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class SettingsViewModelTest {

    @Rule
    public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();

    private SettingsViewModel settingsViewModel;

    private final ConfigRepository configRepository = Mockito.mock(ConfigRepository.class);

    @Before
    public void setup(){
        settingsViewModel = new SettingsViewModel(configRepository);
    }

    @Test
    public void readRadiusValueShouldReturnValueWhenSuccess() {
        String radius = "123";
        when(configRepository.getRadius()).thenReturn(radius);

        String retrievedRadius = settingsViewModel.readRadiusValue();

        Assert.assertEquals(radius, retrievedRadius);
    }

    @Test
    public void readNotificationsValueShouldReturnValueWhenSuccess() {
        Boolean value = true;
        when(configRepository.getNotificationsPreferences()).thenReturn(value);

        Boolean retrievedValue = settingsViewModel.readNotificationPreferenceValue();

        Assert.assertEquals(value, retrievedValue);
    }
}
