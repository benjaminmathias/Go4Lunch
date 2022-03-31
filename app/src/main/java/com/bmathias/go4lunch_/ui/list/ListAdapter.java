package com.bmathias.go4lunch_.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

    private List<RestaurantApi> restaurantItemsList;
    private final OnRestaurantListener mOnRestaurantListener;

    public ListAdapter(List<RestaurantApi> restaurantItemsList, OnRestaurantListener onRestaurantListener) {
        this.restaurantItemsList = restaurantItemsList;
        this.mOnRestaurantListener = onRestaurantListener;
    }

    public void setRestaurantItems(List<RestaurantApi> restaurantItems){
        this.restaurantItemsList = restaurantItems;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), mOnRestaurantListener);
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

        // For testing purpose
        holder.binding.restaurantImageView.setImageResource(R.drawable.ic_baseline_fastfood_24);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnRestaurantListener.onRestaurantClick(restaurant.getPlaceId());
            }
        });
       /*
        if (restaurant.getPhotos() != null) {
            String photoAttribute = restaurant.getPhotos().get(0).getPhotoReference();

            String apiUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";

            String photoUrl = apiUrl + photoAttribute + "&key=" + BuildConfig.MAPS_API_KEY;
            Glide.with(holder.binding.getRoot())
                    .load(photoUrl)
                    .apply(RequestOptions.centerCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(holder.binding.restaurantImageView);
        } else {
            holder.binding.restaurantImageView.setImageResource(R.drawable.ic_baseline_fastfood_24);
        }
*/
    }

    public interface OnRestaurantListener {
        void onRestaurantClick(String placeId);
    }

    @Override
    public int getItemCount() {
        return restaurantItemsList == null ? 0 : restaurantItemsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final FragmentListItemBinding binding;
        OnRestaurantListener onRestaurantListener;

        public ViewHolder(FragmentListItemBinding binding, OnRestaurantListener onRestaurantListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onRestaurantListener = onRestaurantListener;

        }

    }
}
