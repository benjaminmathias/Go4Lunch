package com.bmathias.go4lunch_;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bmathias.go4lunch_.databinding.ActivitySettingsBinding;
import com.bmathias.go4lunch_.injection.Injection;
import com.bmathias.go4lunch_.injection.ViewModelFactory;
import com.bmathias.go4lunch_.viewmodel.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
        selectSpinnerValue(binding.restaurantDistanceSpinner, this.settingsViewModel.readSharedPreferences("radius", "1000"));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        this.settingsViewModel.writeSharedPreferences("radius", text);
        Log.d("Settings Activity", "SharedPreferences selected : " + this.settingsViewModel.readSharedPreferences("radius", text));
    }

    private void selectSpinnerValue(Spinner spinner, String myString)
    {
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equals(myString)){
                spinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}