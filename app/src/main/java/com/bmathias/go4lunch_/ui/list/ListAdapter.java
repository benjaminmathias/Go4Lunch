package com.bmathias.go4lunch_.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmathias.go4lunch_.BuildConfig;
import com.bmathias.go4lunch_.MainActivity;
import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.data.network.model.places.OpeningHours;
import com.bmathias.go4lunch_.data.network.model.places.RestaurantApi;
import com.bmathias.go4lunch_.databinding.FragmentListItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final List<RestaurantApi> restaurantItemsList;

    public ListAdapter(List<RestaurantApi> restaurantItemsList) {
        this.restaurantItemsList = restaurantItemsList;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {

        // Setup name textview
        RestaurantApi restaurant = restaurantItemsList.get(position);
        holder.binding.restaurantNameTextView.setText(String.valueOf(restaurant.getName()));

        // Setup address textview
        holder.binding.restaurantTypeAndAddressTextView.setText(String.valueOf(restaurant.getVicinity()));

        // Setup status
        OpeningHours openStatus = restaurant.getOpeningHours();
        if (openStatus != null && openStatus.getOpenNow()) {
            holder.binding.restaurantOpenTextView.setText("Ouvert");
        } else {
            holder.binding.restaurantOpenTextView.setText("Fermé");
        }

        // Setup ImageView
        if (restaurant.getPhotos() != null) {
            String photoAttribute = restaurant.getPhotos().get(0).getPhotoReference();

            String apiUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";

          //  https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=Aap_uEBx7o06Lk6lOjzJs-xBiMDsavziD8CgOAuVzRWqx-5x3_&key=AIzaSyDKVEFMvGvHtXCQeWzF1_xYjVDHLuikiCE

            String photoUrl = apiUrl + photoAttribute + "&key=" + BuildConfig.MAPS_API_KEY;
            Glide.with(holder.binding.getRoot())
                    .load(photoUrl)
                    .apply(RequestOptions.centerCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(holder.binding.restaurantImageView);
        } else {
            holder.binding.restaurantImageView.setImageResource(R.drawable.ic_baseline_fastfood_24);
        }

        // Setup onClickListener
        holder.binding.restaurantListLayout.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), DetailsActivity.class);
            Toast.makeText(view.getContext(), "Click on " + restaurant.getName(), Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putString("placeId", restaurant.getPlaceId());
            view.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return restaurantItemsList == null ? 0 : restaurantItemsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final FragmentListItemBinding binding;

        public ViewHolder(FragmentListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
