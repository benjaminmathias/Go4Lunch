package com.bmathias.go4lunch_.ui.list;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.data.model.RestaurantDetails;
import com.bmathias.go4lunch_.databinding.ActivityDetailsBinding;
import com.bmathias.go4lunch_.injection.Injection;
import com.bmathias.go4lunch_.injection.ViewModelFactory;
import com.bmathias.go4lunch_.viewmodel.DetailsViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    private DetailsViewModel detailsViewModel;
    private ActivityDetailsBinding binding;

    private Boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String placeId = getIntent().getExtras().getString("placeId");
        this.setupDetailsViewModel(placeId);
        Objects.requireNonNull(getSupportActionBar()).hide();
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.setupDetailsView();
        setContentView(view);
    }

    private void setupDetailsViewModel(String placeId) {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        this.detailsViewModel = new ViewModelProvider(this, viewModelFactory).get(DetailsViewModel.class);
        this.detailsViewModel.observeRestaurantDetails(placeId);
    }

    private void setupDetailsView() {
        detailsViewModel.getRestaurantDetails().observe(this, restaurantDetails -> {
            if (restaurantDetails == null) {
                return;
            }

            restaurantDetailsPhoneDialog(restaurantDetails);
            restaurantDetailsWebsiteDialog(restaurantDetails);
            restaurantDetailsImageView(restaurantDetails);
            restaurantDetailsTextViews(restaurantDetails);
        });
    }

    private void restaurantDetailsTextViews(RestaurantDetails restaurantDetails){
        binding.restaurantDetailsName.setText(Objects.requireNonNull(restaurantDetails.getName()));
        binding.restaurantDetailsAddress.setText(Objects.requireNonNull(restaurantDetails.getAddress()));
    }

    // Setup an AlertDialog to call the corresponding restaurant IF a phone number is available
    private void restaurantDetailsPhoneDialog(RestaurantDetails restaurantDetails) {

        String phoneNumber = restaurantDetails.getPhoneNumber();

        if (phoneNumber != null) {
            binding.restaurantDetailsPhone.setOnClickListener(view -> new AlertDialog.Builder(DetailsActivity.this)
                    .setMessage("Whould you like to call " + restaurantDetails.getName() + " at the following number : " + phoneNumber)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                        startActivity(phoneIntent);
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel())
                    .show());
        } else {
            binding.restaurantDetailsPhone.setEnabled(false);
        }
    }

    // Setup an AlertDialog to open the restaurant's website IF there's a website available
    private void restaurantDetailsWebsiteDialog(RestaurantDetails restaurantDetails) {
        if (restaurantDetails.getWebsite() != null) {
            binding.restaurantDetailsWebsite.setOnClickListener(view -> new AlertDialog.Builder(DetailsActivity.this)
                    .setMessage("Whould you like to open " + restaurantDetails.getName() + " website ?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        Uri webpage = Uri.parse(restaurantDetails.getWebsite());
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                        startActivity(webIntent);
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel())
                    .show());
        }
    }

    // Setup ImageView resource
    private void restaurantDetailsImageView(RestaurantDetails restaurantDetails){
        if (restaurantDetails.getPhotoUrl() != null) {
            Glide.with(binding.getRoot())
                    .load(restaurantDetails.getPhotoUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(binding.restaurantDetailsPicture);
        } else {
            binding.restaurantDetailsPicture.setImageResource(R.drawable.ic_baseline_fastfood_24);
        }

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