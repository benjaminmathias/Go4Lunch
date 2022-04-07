package com.bmathias.go4lunch_;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bmathias.go4lunch_.data.manager.UserManager;
import com.bmathias.go4lunch_.databinding.ActivityMainBinding;
import com.bmathias.go4lunch_.ui.workmates.WorkmatesFragment;
import com.bmathias.go4lunch_.ui.list.ListFragment;
import com.bmathias.go4lunch_.ui.map.MapFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private UserManager userManager = UserManager.getInstance();

    private ActivityMainBinding binding;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationBarView bottomNavigationView;
    private NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        configureToolbar();
        configureNavigationView();
        configureDrawerLayout();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ListFragment()).commit();

        updateUIWithUserData();
    }

    @SuppressLint("NonConstantResourceId")
    private final NavigationBarView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.nav_map:
                selectedFragment = new MapFragment();
                break;
            case R.id.nav_list:
                selectedFragment = new ListFragment();
                break;
            case R.id.nav_workmates:
                selectedFragment = new WorkmatesFragment();
                break;
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
        }

        return true;
    };

    private void configureToolbar() {
        this.toolbar = this.binding.activityMainToolbar;
        setSupportActionBar(toolbar);
    }

    private void configureNavigationView() {
        this.bottomNavigationView = this.binding.bottomNavigation;
        bottomNavigationView.setOnItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_list);
    }

    private void configureDrawerLayout() {
        this.drawerLayout = this.binding.activityMainDrawerLayout;

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.button_login_text_logged, R.string.button_login_text_not_logged);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    private void updateUIWithUserData(){
        if (userManager.isCurrentUserLogged()) {
            FirebaseUser user = userManager.getCurrentUser();

            if (user.getPhotoUrl() != null){
                setProfilePicture(user.getPhotoUrl());
            }

            setTextUserData(user);
        }
    }

    private void setProfilePicture(Uri profilePictureUrl){
        this.navigationView = this.binding.navigationView;
        View headerView = binding.navigationView.getHeaderView(0);
        ImageView userImageView = headerView.findViewById(R.id.nav_header_user_image_view);
        Glide.with(this)
                .load(profilePictureUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_settings)
                .apply(RequestOptions.circleCropTransform())
                .into(userImageView);

    }

    private void setTextUserData(FirebaseUser user){
        //Get email & username from User
        String email = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.info_no_email_found) : user.getEmail();
        String userFirstName = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.info_no_username_found) : user.getDisplayName();

        this.navigationView = this.binding.navigationView;
        View headerView = binding.navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.nav_header_user_name_text_view);
        TextView userMailTextView = headerView.findViewById(R.id.nav_header_user_mail_text_view);
        userNameTextView.setText(userFirstName);
        userMailTextView.setText(email);

    }
}