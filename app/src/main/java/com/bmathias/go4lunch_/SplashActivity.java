package com.bmathias.go4lunch_;

import static com.bmathias.go4lunch_.utils.Constants.USER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

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

        initSplashViewModel();

        Objects.requireNonNull(getSupportActionBar()).hide();

        progressBarSetup();

        new Handler().postDelayed(this::checkIfUserIsAuthenticated, 2100);
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