package com.bmathias.go4lunch_;

import static com.bmathias.go4lunch_.utils.Constants.USER;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.databinding.ActivitySplashBinding;
import com.bmathias.go4lunch_.injection.Injection;
import com.bmathias.go4lunch_.injection.ViewModelFactory;
import com.bmathias.go4lunch_.viewmodel.SplashViewModel;

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

        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
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
                            progressBarSetup();
                            new Handler().postDelayed(this::checkIfUserIsAuthenticated, 2100);
                        } else if (Boolean.FALSE.equals(fineLocationGranted) && Boolean.FALSE.equals(coarseLocationGranted))  {
                            // No location access granted.
                            Toast.makeText(this, "You need to authorize location access to use this app !", Toast.LENGTH_LONG).show();
                            result.put(Manifest.permission.ACCESS_FINE_LOCATION, false);
                            result.put(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            new Handler().postDelayed(this::finish, 2000);
                            startActivity(getIntent());
                        }
                    }
            );

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