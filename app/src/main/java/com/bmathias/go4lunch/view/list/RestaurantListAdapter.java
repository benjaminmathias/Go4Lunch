package com.bmathias.go4lunch.view.list;

import static androidx.navigation.Navigation.findNavController;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bmathias.go4lunch.R;
import com.bmathias.go4lunch.data.model.Restaurant;
import com.bmathias.go4lunch.network.model.RestaurantPlaceResponse.OpeningHours;
import com.bmathias.go4lunch.network.model.RestaurantPlaceResponse.Photo;
import com.bmathias.go4lunch.network.model.RestaurantPlaceResponse.RestaurantAPI;
import com.bmathias.go4lunch.databinding.FragmentRestaurantlistItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    private List<Restaurant> restaurantItemsList;
    private FragmentActivity activity;
    private RequestManager glide;
    NavController navController;

    public RestaurantListAdapter(List<Restaurant> restaurantItemsList, FragmentActivity activity, RequestManager glide) {
        this.restaurantItemsList = restaurantItemsList;
        this.activity = activity;
        this.glide = glide;
    }

    @NonNull
    @Override
    public RestaurantListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentRestaurantlistItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    void updateRestaurants(@NonNull final List<Restaurant> restaurantItemsList){
        this.restaurantItemsList = restaurantItemsList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantListAdapter.ViewHolder holder, int position) {

        // Setup name textview
        Restaurant restaurant = restaurantItemsList.get(position);
        holder.binding.restaurantNameTextView.setText(String.valueOf(restaurant.getName()));

        // Setup address textview
        holder.binding.restaurantTypeAndAddressTextView.setText(String.valueOf(restaurant.getAddress()));

        // Setup status
        OpeningHours openStatus = restaurant.getIsOpen();
        if (openStatus != null && openStatus.getOpenNow()) {
            holder.binding.restaurantOpenTextView.setText("Ouvert");
        } else {
            holder.binding.restaurantOpenTextView.setText("Fermé");
        }

        // Setup ImageView
        if (restaurant.getPhoto() != null && restaurant.getPhoto() != null) {
            String photoAttribute = restaurant.getPhoto();
            StringBuilder getPhoto_URL = new StringBuilder();

            String API_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
            String API_Key = "&key=AIzaSyDKVEFMvGvHtXCQeWzF1_xYjVDHLuikiCE";

            getPhoto_URL.append(API_url).append(photoAttribute).append(API_Key);

            Glide.with(holder.binding.getRoot())
                    .load(getPhoto_URL)
                    .apply(RequestOptions.centerCropTransform())
                    .into(holder.binding.restaurantImageView);
        } else {
            holder.binding.restaurantImageView.setImageResource(R.drawable.ic_lunch);
        }

        // Setup onClickListener
        holder.binding.restaurantListLayout.setOnClickListener(view -> {
            Toast.makeText(activity.getApplicationContext(), "Click on " + restaurant.getName(), Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putString("placeId", restaurant.getPlaceId());
            navController = findNavController(activity, R.id.fragment);
            navController.navigate(R.id.detailsFragment, bundle);
        });


    }

    @Override
    public int getItemCount() {
        return restaurantItemsList == null ? 0 : restaurantItemsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final FragmentRestaurantlistItemBinding binding;

        public ViewHolder(FragmentRestaurantlistItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
