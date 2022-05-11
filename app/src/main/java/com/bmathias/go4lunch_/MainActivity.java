package com.bmathias.go4lunch_;


import static com.bmathias.go4lunch_.utils.Constants.USER;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.databinding.ActivityMainBinding;
import com.bmathias.go4lunch_.injection.Injection;
import com.bmathias.go4lunch_.injection.ViewModelFactory;
import com.bmathias.go4lunch_.ui.list.DetailsActivity;
import com.bmathias.go4lunch_.ui.list.ListFragment;
import com.bmathias.go4lunch_.ui.map.MapFragment;
import com.bmathias.go4lunch_.ui.workmates.WorkmatesFragment;
import com.bmathias.go4lunch_.viewmodel.MainViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private GoogleSignInClient googleSignInClient;

    private ActivityMainBinding binding;

    private Toolbar toolbar;
    private NavigationView navigationView;

    private MainViewModel mainViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        User user = getUserFromIntent();
        initGoogleSignInClient();

        setupMainViewModel();

        configureToolbar();
        configureNavigationView();
        configureDrawerLayout();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ListFragment()).commit();

        updateUIWithUserData(user);
    }

    private User getUserFromIntent() {
        return (User) getIntent().getSerializableExtra(USER);
    }

    private void setupMainViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        this.mainViewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
        this.mainViewModel.getUserFromDatabase();
    }

    private void initGoogleSignInClient() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            goToAuthInActivity();
        }
    }

    private void goToAuthInActivity() {
        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(intent);
    }

    private void signOut() {
        singOutFirebase();
        signOutGoogle();
    }

    private void singOutFirebase() {
        firebaseAuth.signOut();
    }

    private void signOutGoogle() {
        googleSignInClient.signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    private final NavigationBarView.OnItemSelectedListener bottomNavListener = item -> {
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

    @SuppressLint("NonConstantResourceId")
    private final NavigationView.OnNavigationItemSelectedListener drawerNavListener = item -> {

        switch (item.getItemId()) {
            case R.id.your_lunch_button:

                mainViewModel.getUserFromDatabase();
                this.mainViewModel.currentUser.observe(this, user -> {
                    Toast.makeText(this, "Vous avez choisi le restaurant suivant :" + user.getSelectedRestaurantName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, DetailsActivity.class);
                    intent.putExtra("placeId", user.getSelectedRestaurantId());
                    startActivity(intent);
                });

                break;
            case R.id.settings_button:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();

                break;
            case R.id.logout_button:
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Would you like to log out ?")
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            signOut();
                            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel())
                        .show();

                break;
        }
        return true;
    };


    private void configureToolbar() {
        this.toolbar = this.binding.activityMainToolbar;
        setSupportActionBar(toolbar);
    }

    private void configureNavigationView() {
        NavigationBarView bottomNavigationView = this.binding.bottomNavigation;
        bottomNavigationView.setOnItemSelectedListener(bottomNavListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_list);

        this.navigationView = this.binding.navigationView;
        navigationView.setNavigationItemSelectedListener(drawerNavListener);
    }

    private void configureDrawerLayout() {
        DrawerLayout drawerLayout = this.binding.activityMainDrawerLayout;

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.button_login_text_logged, R.string.button_login_text_not_logged);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    private void updateUIWithUserData(User user) {
        if (user.getPhotoUrl() != null) {
            setProfilePicture(user.getPhotoUrl());
        }
        setTextUserData(user);
    }

    private void setProfilePicture(String profilePictureUrl) {
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

    private void setTextUserData(User user) {
        //Get email & username from User
        String email = TextUtils.isEmpty(user.getUserEmail()) ? getString(R.string.info_no_email_found) : user.getUserEmail();
        String userFirstName = TextUtils.isEmpty(user.getUserName()) ? getString(R.string.info_no_username_found) : user.getUserName();

        this.navigationView = this.binding.navigationView;
        View headerView = binding.navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.nav_header_user_name_text_view);
        TextView userMailTextView = headerView.findViewById(R.id.nav_header_user_mail_text_view);
        userNameTextView.setText(userFirstName);
        userMailTextView.setText(email);

    }

}