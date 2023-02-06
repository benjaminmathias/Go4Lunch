package com.bmathias.go4lunch.ui.list;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bmathias.go4lunch.R;
import com.bmathias.go4lunch.data.model.RestaurantDetails;
import com.bmathias.go4lunch.databinding.ActivityDetailsBinding;
import com.bmathias.go4lunch.injection.Injection;
import com.bmathias.go4lunch.injection.ViewModelFactory;
import com.bmathias.go4lunch.ui.MainActivity;
import com.bmathias.go4lunch.utils.NotificationReceiver;
import com.bmathias.go4lunch.viewmodel.DetailsViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class DetailsActivity extends AppCompatActivity {

    private DetailsViewModel detailsViewModel;
    private DetailsAdapter adapter;
    private ActivityDetailsBinding binding;

    private String placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String placeId = getIntent().getExtras().getString("placeId");
        this.setupDetailsViewModel(placeId);
        this.setupLike(placeId);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        configureToolbar();

        this.setupDetailsView();
        this.setupFab(placeId);
        this.setupRecyclerView();
        setContentView(view);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(DetailsActivity.this, MainActivity.class));
            finish();
        }
        return true;
    }

    private void configureToolbar() {
        Toolbar toolbar = this.binding.activityDetailsToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupDetailsViewModel(String placeId) {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        this.detailsViewModel = new ViewModelProvider(this, viewModelFactory).get(DetailsViewModel.class);
        this.detailsViewModel.observeRestaurantsDetails(placeId);
        this.detailsViewModel.getSpecificUsersFromDatabase(placeId);
    }

    private void setupRecyclerView() {
        adapter = new DetailsAdapter(new ArrayList<>());

        this.detailsViewModel.getSpecificUsers().observe(this, users ->
                adapter.setUserItems(users));

        binding.restaurantDetailsWorkmatesRecyclerview.setAdapter(adapter);
        binding.restaurantDetailsWorkmatesRecyclerview.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }

    // Setup FAB style when activity is opened
    private void setupFab(String placeId) {

        detailsViewModel.getUserFromDatabase();
        this.detailsViewModel.currentUser.observe(this, user -> {

            AtomicBoolean fabIsClicked = new AtomicBoolean(false);
            if (user.getSelectedRestaurantId() != null && user.getSelectedRestaurantId().equals(placeId)) {
                binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_check_24);
                fabIsClicked.set(true);
            } else {
                binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_group_24);
                fabIsClicked.set(false);
            }

            binding.floatingActionButton.setOnClickListener(view -> {
                if (fabIsClicked.getAndSet(true)) {
                    binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_group_24);
                    detailsViewModel.deleteSelectedRestaurant();
                    fabIsClicked.set(false);
                } else {
                    binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_check_24);
                    detailsViewModel.updateSelectedRestaurant(placeId, placeName);
                    createNotificationChannel();
                    setupNotification(placeId);
                    fabIsClicked.set(true);
                }
            });
        });
    }

    private void setupDetailsView() {
        detailsViewModel.getRestaurantDetails().observe(this, restaurantDetails -> {
            if (restaurantDetails == null) {
                return;
            }

            placeName = restaurantDetails.getName();
            restaurantDetailsPhoneDialog(restaurantDetails);
            restaurantDetailsWebsiteDialog(restaurantDetails);
            restaurantDetailsImageView(restaurantDetails);
            restaurantDetailsTextViews(restaurantDetails);

            binding.restaurantDetailsPhone.setText(R.string.details_call_button);
            binding.restaurantDetailsWebsite.setText(R.string.details_website_button);
            binding.restaurantDetailsLike.setText(R.string.details_like_button);
        });
    }

    private void restaurantDetailsTextViews(RestaurantDetails restaurantDetails) {
        binding.restaurantDetailsName.setText(Objects.requireNonNull(restaurantDetails.getName()));
        binding.restaurantDetailsAddress.setText(Objects.requireNonNull(restaurantDetails.getAddress()));
    }

    // Setup an AlertDialog to call the corresponding restaurant IF a phone number is available
    private void restaurantDetailsPhoneDialog(RestaurantDetails restaurantDetails) {

        String phoneNumber = restaurantDetails.getPhoneNumber();

        if (phoneNumber != null) {
            binding.restaurantDetailsPhone.setOnClickListener(view -> new AlertDialog.Builder(DetailsActivity.this)
                    .setMessage(this.getResources().getString(R.string.details_dialog_phone_message_start) + restaurantDetails.getName() + this.getResources().getString(R.string.details_dialog_phone_message_end) + phoneNumber)
                    .setPositiveButton(R.string.positive_string, (dialogInterface, i) -> {
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                        startActivity(phoneIntent);
                    })
                    .setNegativeButton(R.string.negative_string, (dialogInterface, i) -> dialogInterface.cancel())
                    .show());
        } else {
            binding.restaurantDetailsPhone.setEnabled(false);
            binding.restaurantDetailsPhone.setTextColor(Color.LTGRAY);
        }
    }

    private void setupLike(String placeId) {

        detailsViewModel.getUserFromDatabase();

        this.detailsViewModel.getRestaurantDetails().observe(this, restaurant -> {

            if (restaurant == null) {
                return;
            }

            AtomicBoolean likeIsClicked = new AtomicBoolean(restaurant.getCurrentUserFavorite());
            if (restaurant.getCurrentUserFavorite()) {
                likeIsClicked.set(true);
                tintViewDrawable(binding.restaurantDetailsLike, likeIsClicked.getAndSet(true));
            } else {
                likeIsClicked.set(false);
                tintViewDrawable(binding.restaurantDetailsLike, likeIsClicked.getAndSet(false));
            }

            binding.restaurantDetailsLike.setOnClickListener(view -> {
                if (likeIsClicked.getAndSet(true)) {
                    detailsViewModel.deleteFavoriteRestaurant(placeId);
                    likeIsClicked.set(false);
                } else {
                    detailsViewModel.addFavoriteRestaurant(placeId);
                    likeIsClicked.set(true);
                }
                tintViewDrawable(binding.restaurantDetailsLike, likeIsClicked.get());
            });
        });
    }

    // Setup an AlertDialog to open the restaurant's website IF there's a website available
    private void restaurantDetailsWebsiteDialog(RestaurantDetails restaurantDetails) {
        if (restaurantDetails.getWebsite() != null) {
            binding.restaurantDetailsWebsite.setOnClickListener(view -> new AlertDialog.Builder(DetailsActivity.this)
                    .setMessage(this.getResources().getString(R.string.details_dialog_web_message_start) + restaurantDetails.getName() + this.getResources().getString(R.string.details_dialog_web_message_end))
                    .setPositiveButton(R.string.positive_string, (dialogInterface, i) -> {
                        Uri webpage = Uri.parse(restaurantDetails.getWebsite());
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                        startActivity(webIntent);
                    })
                    .setNegativeButton(R.string.negative_string, (dialogInterface, i) -> dialogInterface.cancel())
                    .show());
        } else {
            binding.restaurantDetailsWebsite.setEnabled(false);
            binding.restaurantDetailsWebsite.setTextColor(Color.LTGRAY);
        }
    }

    // Setup ImageView resource
    private void restaurantDetailsImageView(RestaurantDetails restaurantDetails) {

       binding.restaurantDetailsPicture.setImageResource(R.drawable.drawer_image);

        // hungry in api call since we're using google places api, commented to avoid reaching daily quotas quickly
/*
        if (restaurantDetails.getPhotoUrl() != null) {
            Log.d("Details Photo", restaurantDetails.getPhotoUrl());
            Glide.with(binding.getRoot())
                    .load(restaurantDetails.getPhotoUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(binding.restaurantDetailsPicture);
        } else {
            binding.restaurantDetailsPicture.setImageResource(R.drawable.drawer_image);
        }
*/

    }

    private void tintViewDrawable(Button button, boolean isClicked) {
        Drawable[] drawables = button.getCompoundDrawables();
        for (Drawable drawable : drawables) {
            if (drawable != null && !isClicked) {
                drawable.setColorFilter(getResources().getColor(R.color.orange_500), PorterDuff.Mode.SRC_ATOP);
            } else if (drawable != null) {
                drawable.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = String.valueOf(R.string.app_name);
            String description = "Channel for Go4Lunch Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyGo4Lunch", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void setupNotification(String placeId) {
        if (detailsViewModel.retrieveNotificationsPreferences()) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            if (calendar.getTime().compareTo(new Date()) < 0) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            Intent intent = new Intent(DetailsActivity.this, NotificationReceiver.class);
            intent.putExtra("placeId", placeId);
            PendingIntent pendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                pendingIntent = PendingIntent.getBroadcast(this,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            } else {
                pendingIntent = PendingIntent.getBroadcast(this,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                Log.d("AlarmManager", "Notification set !");
            }
        }
    }
}