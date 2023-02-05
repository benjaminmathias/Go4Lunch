package com.bmathias.go4lunch_.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.databinding.ActivitySettingsBinding;
import com.bmathias.go4lunch_.injection.Injection;
import com.bmathias.go4lunch_.injection.ViewModelFactory;
import com.bmathias.go4lunch_.viewmodel.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private ActivitySettingsBinding binding;

    private SettingsViewModel settingsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setupViewModel();
        setupSpinner();
        setupCheckbox();
    }

    private void setupViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        this.settingsViewModel = new ViewModelProvider(this, viewModelFactory).get(SettingsViewModel.class);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.distance, R.layout.spinner_style);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.restaurantDistanceSpinner.setAdapter(adapter);
        binding.restaurantDistanceSpinner.setOnItemSelectedListener(this);
        selectSpinnerValue(binding.restaurantDistanceSpinner, this.settingsViewModel.readRadiusValue());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        this.settingsViewModel.writeRadius(text);
        Log.d("Settings Activity", "SharedPreferences selected : " + this.settingsViewModel.readRadiusValue());
    }

    private void selectSpinnerValue(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(myString)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void setupCheckbox() {
        binding.notificationsSettingsCheckbox.setChecked(this.settingsViewModel.readNotificationPreferenceValue());
        binding.notificationsSettingsCheckbox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView.getId() == R.id.notifications_settings_checkbox) {
            if (buttonView.isChecked()) {
                this.settingsViewModel.setNotificationPreference(true);
                Log.d("CheckBox Value", "true");
                Toast.makeText(this, "True", Toast.LENGTH_SHORT).show();
            } else {
                this.settingsViewModel.setNotificationPreference(false);
                Log.d("CheckBox Value", "false");
                Toast.makeText(this, "False", Toast.LENGTH_SHORT).show();
            }
        }
    }


}