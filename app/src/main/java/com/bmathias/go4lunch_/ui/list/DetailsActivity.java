package com.bmathias.go4lunch_.ui.list;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.data.model.RestaurantDetails;
import com.bmathias.go4lunch_.databinding.ActivityDetailsBinding;
import com.bmathias.go4lunch_.injection.Injection;
import com.bmathias.go4lunch_.injection.ViewModelFactory;
import com.bmathias.go4lunch_.viewmodel.DetailsViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    private DetailsViewModel detailsViewModel;
    private DetailsAdapter adapter;
    private ActivityDetailsBinding binding;

    private Boolean isClicked;
    private String placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String placeId = getIntent().getExtras().getString("placeId");
        this.setupDetailsViewModel(placeId);
        Objects.requireNonNull(getSupportActionBar()).hide();
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.setupDetailsView();
        this.setupLike(placeId);
        this.setupFab(placeId);
        this.setupRecyclerView();
        setContentView(view);
    }

    private void setupDetailsViewModel(String placeId) {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        this.detailsViewModel = new ViewModelProvider(this, viewModelFactory).get(DetailsViewModel.class);
        this.detailsViewModel.observeRestaurantDetails(placeId);
        this.detailsViewModel.getSpecificUsersFromDatabase(placeId);
    }

    private void setupRecyclerView() {
        adapter = new DetailsAdapter(new ArrayList<>());

        this.detailsViewModel.getSpecificUsers().observe(this, users ->
                adapter.setUserItems(users));

        binding.restaurantDetailsWorkmatesRecyclerview.setAdapter(adapter);
        binding.restaurantDetailsWorkmatesRecyclerview.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }

    private void setupFab(String placeId) {

        detailsViewModel.getUserFromDatabase();
        this.detailsViewModel.currentUser.observe(this, user -> {

            // Setup FAB style when activity is opened
            if (user.getSelectedRestaurantId() != null && user.getSelectedRestaurantId().equals(placeId)) {
                binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_check_24);
                isClicked = true;
            } else {
                binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_group_24);
                isClicked = false;
            }

            binding.floatingActionButton.setOnClickListener(view -> {
                if (isClicked) {
                    binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_group_24);
                    detailsViewModel.deleteSelectedRestaurant();
                    isClicked = false;
                } else {
                    binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_check_24);
                    detailsViewModel.updateSelectedRestaurant(placeId, placeName);
                    isClicked = true;
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
                    .setMessage("Would you like to call " + restaurantDetails.getName() + " at the following number : " + phoneNumber)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                        startActivity(phoneIntent);
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel())
                    .show());
        } else {
            binding.restaurantDetailsPhone.setEnabled(false);
            binding.restaurantDetailsPhone.setTextColor(Color.LTGRAY);
        }
    }

    private void setupLike(String placeId) {

        detailsViewModel.getUserFromDatabase();

        this.detailsViewModel.getRestaurantDetails().observe(this, restaurant -> {

            isClicked = restaurant.getCurrentUserFavorite();
            tintViewDrawable(binding.restaurantDetailsLike, isClicked);

            binding.restaurantDetailsLike.setOnClickListener(view -> {
                if (isClicked) {
                    tintViewDrawable(binding.restaurantDetailsLike, false);
                    detailsViewModel.deleteFavoriteRestaurant(placeId);
                    isClicked = false;
                } else {
                    tintViewDrawable(binding.restaurantDetailsLike, true);
                    detailsViewModel.addFavoriteRestaurant(placeId);
                    isClicked = true;
                }
            });
        });
    }


    // Setup an AlertDialog to open the restaurant's website IF there's a website available
    private void restaurantDetailsWebsiteDialog(RestaurantDetails restaurantDetails) {
        if (restaurantDetails.getWebsite() != null) {
            binding.restaurantDetailsWebsite.setOnClickListener(view -> new AlertDialog.Builder(DetailsActivity.this)
                    .setMessage("Would you like to open " + restaurantDetails.getName() + " website ?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        Uri webpage = Uri.parse(restaurantDetails.getWebsite());
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                        startActivity(webIntent);
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel())
                    .show());
        } else {
            binding.restaurantDetailsWebsite.setEnabled(false);
            binding.restaurantDetailsWebsite.setTextColor(Color.LTGRAY);
        }
    }

    // Setup ImageView resource
    private void restaurantDetailsImageView(RestaurantDetails restaurantDetails) {

        binding.restaurantDetailsPicture.setImageResource(R.drawable.drawer_image);

        // TODO : image won't load for testing purpose
        /*
        if (restaurantDetails.getPhotoUrl() != null) {
            Glide.with(binding.getRoot())
                    .load(restaurantDetails.getPhotoUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(binding.restaurantDetailsPicture);
        } else {
            binding.restaurantDetailsPicture.setImageResource(R.drawable.ic_baseline_fastfood_24);
        }
        */

        // Like Button
        binding.restaurantDetailsLike.setOnClickListener(view -> {
            isClicked = !isClicked;
            tintViewDrawable(binding.restaurantDetailsLike, isClicked);
        });
    }

    private void tintViewDrawable(Button button, Boolean isClicked) {
        Drawable[] drawables = button.getCompoundDrawables();
        for (Drawable drawable : drawables) {
            if (drawable != null && !isClicked) {
                drawable.setColorFilter(getResources().getColor(R.color.orange_500), PorterDuff.Mode.SRC_ATOP);
            } else if (drawable != null) {
                drawable.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }
}