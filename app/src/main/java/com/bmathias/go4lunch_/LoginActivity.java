package com.bmathias.go4lunch_;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.bmathias.go4lunch_.databinding.ActivityLoginBinding;
import com.bmathias.go4lunch_.databinding.ActivityMainBinding;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private static final int RC_SIGN_IN = 123;


    @Override
    ActivityLoginBinding getViewBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ONLY FOR TEST, TO REMOVE
        //  testButton();
         setupLoginListeners();



    }

    // Used for dev only
    // TODO: remove when login done
 /*   private void testButton(){

        loginBinding.button.setOnClickListener(v -> {

           startMainActivity();
        });
    }*/

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void setupLoginListeners(){
        // Google Login Button
        binding.googleLoginButton.setOnClickListener(view -> {
            startSignInActivity();
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // Show Snack Bar with a message
    private void showSnackBar(String message) {
        View mainActivityLayout = findViewById(R.id.login_activity_layout);
        Snackbar.make(mainActivityLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void startSignInActivity() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers =
                Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());

        // Launch the activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_baseline_fastfood_24)
                        .build(),
                RC_SIGN_IN);
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            // SUCCESS
            if (resultCode == RESULT_OK) {
                // userManager.createUser();
                showSnackBar(getString(R.string.connection_succeed));
                startMainActivity();
            } else {
                // ERRORS
                if (response == null) {
                    showSnackBar(getString(R.string.error_authentication_canceled));
                } else if (response.getError() != null) {
                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showSnackBar(getString(R.string.error_no_internet));
                    } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar(getString(R.string.error_unknown_error));
                    }
                }
            }
        }
    }
}