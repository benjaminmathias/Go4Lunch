package com.bmathias.go4lunch_.ui.list;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.bmathias.go4lunch_.BuildConfig;
import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.data.network.model.placesDetails.RestaurantDetailsAPI;
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
        getSupportActionBar().hide();
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        String placeId = getIntent().getExtras().getString("placeId");
        this.setupDetailsViewModel(placeId);
        this.setupDetailsView();
    }

    private void setupDetailsViewModel(String placeId) {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        this.detailsViewModel = new ViewModelProvider(this, viewModelFactory).get(DetailsViewModel.class);
        this.detailsViewModel.observeRestaurantDetails(placeId);
    }

    private void setupDetailsView() {
        detailsViewModel.getRestaurantDetails().observe(this, restaurantDetails -> {

            restaurantDetailsTextViews(restaurantDetails);
            restaurantDetailsPhoneDialog(restaurantDetails);
            restaurantDetailsWebsiteDialog(restaurantDetails);
            restaurantDetailsImageView(restaurantDetails);
        });

    }

    private void restaurantDetailsTextViews(RestaurantDetailsAPI restaurantDetails){
        binding.restaurantDetailsName.setText(Objects.requireNonNull(restaurantDetails.getName()));
        binding.restaurantDetailsAddress.setText(Objects.requireNonNull(restaurantDetails.getFormattedAddress()));
    }

    // Setup an AlertDialog to call the corresponding restaurant IF a phone number is available
    private void restaurantDetailsPhoneDialog(RestaurantDetailsAPI restaurantDetails) {

        String phoneNumber = restaurantDetails.getInternationalPhoneNumber();

        if (phoneNumber != null) {
            binding.restaurantDetailsPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(DetailsActivity.this)
                            .setMessage("Whould you like to call " + restaurantDetails.getName() + " at the following number : " + restaurantDetails.getFormattedPhoneNumber())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                                    startActivity(phoneIntent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();
                }
            });
        } else {
            binding.restaurantDetailsPhone.setEnabled(false);
        }
    }

    // Setup an AlertDialog to open the restaurant's website IF there's a website available
    private void restaurantDetailsWebsiteDialog(RestaurantDetailsAPI restaurantDetails) {
        if (restaurantDetails.getWebsite() != null) {
            binding.restaurantDetailsWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(DetailsActivity.this)
                            .setMessage("Whould you like to open " + restaurantDetails.getName() + " website ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Uri webpage = Uri.parse(restaurantDetails.getWebsite());
                                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                                    startActivity(webIntent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();
                }
            });
        }
    }

    private void restaurantDetailsImageView(RestaurantDetailsAPI restaurantDetails){
        String photoAttribute = restaurantDetails.getPhotos().get(0).getPhotoReference();
        if (photoAttribute != null) {

            // https://maps.googleapis.com/maps/api/place/photo?photoreference=&sensor=false&maxheight=1000&maxwidth=1000&key=YOUR_API_KEY


            String apiUrl = "https://maps.googleapis.com/maps/api/place/photo?photoreference=";
            String photoDetailsUrl = apiUrl + photoAttribute + "&sensor=false&maxheight=400&maxwidth=600&key=" + BuildConfig.MAPS_API_KEY;
            Glide.with(binding.getRoot())
                    .load(photoDetailsUrl)
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
            } else if (drawable != null && isClicked) {
                drawable.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }
}