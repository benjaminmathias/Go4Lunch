package com.bmathias.go4lunch.view;

import static androidx.navigation.Navigation.findNavController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bmathias.go4lunch.R;
import com.bmathias.go4lunch.data.manager.UserManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

import dagger.hilt.android.AndroidEntryPoint;
import jp.wasabeef.glide.transformations.BlurTransformation;

@AndroidEntryPoint
public class NavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private UserManager userManager = UserManager.getInstance();

    AppBarConfiguration appBarConfiguration;
    NavController navController;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        // Setup Navigation components
        DrawerLayout drawerLayout = findViewById(R.id.nav_activity_drawer_layout);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Toolbar toolbar = findViewById(R.id.activity_nav_toolbar);
        navController = findNavController(this, R.id.fragment);

        appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.listFragment, R.id.mapFragment, R.id.workmatesFragment)
                        .setDrawerLayout(drawerLayout)
                        .build();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        setNavigationViewListener();

        updateUIWithUserData();
    }


    private void updateUIWithUserData() {
        if (userManager.isCurrentUserLogged()) {
            FirebaseUser user = userManager.getCurrentUser();

            if (user.getPhotoUrl() != null) {
                setProfilePicture(user.getPhotoUrl());
            }
            setTextUserData(user);
        }
    }

    private void setProfilePicture(Uri profilePictureUrl) {
        navigationView = findViewById(R.id.activity_nav_nav_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView userImageView = headerView.findViewById(R.id.user_header_image_view);
        Glide.with(this)
                .load(profilePictureUrl)
                .placeholder(R.drawable.ic_workmates)
                .error(R.drawable.ic_settings)
                .apply(RequestOptions.circleCropTransform())
                .into(userImageView);
    }

    private void setTextUserData(FirebaseUser user) {

        //Get email & username from User
        String email = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.info_no_email_found) : user.getEmail();
        String userFirstName = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.info_no_username_found) : user.getDisplayName();

        //Update views with data
        navigationView = findViewById(R.id.activity_nav_nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_header_name_text_view);
        TextView userMailTextView = headerView.findViewById(R.id.user_header_mail_text_view);
        userNameTextView.setText(userFirstName);
        userMailTextView.setText(email);
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout_button: {

                new AlertDialog.Builder(this)
                        .setMessage(R.string.popup_message_informations_account)
                        .setNegativeButton(R.string.popup_message_choice_disconnect, ((dialogInterface, i) ->
                                userManager.signOut(NavActivity.this).addOnSuccessListener(aVoid -> {
                                    returnToMainActivity();
                                })))
                        .setPositiveButton(R.string.popup_message_choice_delete, ((dialogInterface, i) ->
                                userManager.deleteUser(NavActivity.this).addOnSuccessListener(aVoid -> {
                                            returnToMainActivity();
                                        }
                                )))
                        .setNeutralButton(R.string.popup_message_choice_no, null)
                        .show();

                break;
            }

            case R.id.settings_button: {
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.your_lunch_button: {
                Toast.makeText(this, "Your Lunch", Toast.LENGTH_SHORT).show();
                break;
            }

        }
        return true;
    }

    private void returnToMainActivity() {
        Intent mainActivity = new Intent(getApplicationContext(),
                MainActivity.class);
        startActivity(mainActivity);
    }

    private void setNavigationViewListener() {
        navigationView = findViewById(R.id.activity_nav_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

}
