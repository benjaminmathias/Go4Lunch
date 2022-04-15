package com.bmathias.go4lunch_;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.bmathias.go4lunch_.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding activitySplashBinding;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = activitySplashBinding.getRoot();
        setContentView(view);

        getSupportActionBar().hide();

        progressBarSetup();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIfUserIsAuthenticated();
                finish();
            }
        }, 2100);


    }

    private void checkIfUserIsAuthenticated() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, AuthActivity.class));
        }
    }

    private void progressBarSetup(){
        final Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run()
            {
                counter++;
                activitySplashBinding.splashProgressbar.setProgress(counter);

                if(counter == 100)
                    t.cancel();
            }
        };

        t.schedule(tt,0,20);
    }
}