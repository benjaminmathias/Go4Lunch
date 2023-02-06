package com.bmathias.go4lunch.ui;

import static com.bmathias.go4lunch.utils.Constants.USER;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bmathias.go4lunch.BuildConfig;
import com.bmathias.go4lunch.R;
import com.bmathias.go4lunch.data.model.User;
import com.bmathias.go4lunch.databinding.ActivitySplashBinding;
import com.bmathias.go4lunch.injection.Injection;
import com.bmathias.go4lunch.injection.ViewModelFactory;
import com.bmathias.go4lunch.viewmodel.SplashViewModel;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    SplashViewModel splashViewModel;
    ActivitySplashBinding activitySplashBinding;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = activitySplashBinding.getRoot();
        setContentView(view);

        Objects.requireNonNull(getSupportActionBar()).hide();

        initSplashViewModel();

        makePermissionRequest();
    }

    private void initSplashViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        this.splashViewModel = new ViewModelProvider(this, viewModelFactory).get(SplashViewModel.class);

    }

    private void checkIfUserIsAuthenticated() {
        splashViewModel.checkIfUserIsAuthenticated();
        splashViewModel.isUserAuthenticatedLiveData.observe(this, user -> {
            if (user == null) {
                goToAuthInActivity();
            } else {
                goToMainActivity(user);
            }
            finish();
        });
    }

    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {

                        Boolean fineLocationGranted = result.put(Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.put(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                        if (Boolean.TRUE.equals(fineLocationGranted) && Boolean.TRUE.equals(coarseLocationGranted)) {
                            // Location access granted, check if there's an auth user
                            progressBarSetup();
                            new Handler().postDelayed(this::checkIfUserIsAuthenticated, 2100);
                        } else if (Boolean.FALSE.equals(fineLocationGranted) && Boolean.FALSE.equals(coarseLocationGranted)) {

                            // No location access granted.
                            showAlertDialog();

                        }
                    }
            );

    public void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.splash_dialog_message);
        alertDialogBuilder.setPositiveButton(R.string.positive_string,
                (arg0, arg1) -> {
                    try {
                        // Access app settings screen
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",
                                BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } catch (Exception e){
                        Toast.makeText(SplashActivity.this, R.string.splash_dialog_settings_error + " " + e, Toast.LENGTH_LONG).show();
                        Log.d("error", e.toString());
                    }
                });

        alertDialogBuilder.setNegativeButton(R.string.negative_string, (dialog, which) -> {
            Toast.makeText(SplashActivity.this, R.string.splash_dialog_message_warning, Toast.LENGTH_LONG).show();
            finish();
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void makePermissionRequest() {
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    private void goToAuthInActivity() {
        Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
        startActivity(intent);
    }

    private void goToMainActivity(User user) {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra(USER, user);
        startActivity(intent);
    }

    private void progressBarSetup() {
        final Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                counter++;
                activitySplashBinding.splashProgressbar.setProgress(counter);

                if (counter == 100)
                    t.cancel();
            }
        };

        t.schedule(tt, 0, 20);
    }
}