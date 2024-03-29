package com.bmathias.go4lunch.ui.list;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmathias.go4lunch.R;
import com.bmathias.go4lunch.data.model.RestaurantItem;
import com.bmathias.go4lunch.databinding.FragmentListItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Objects;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final List<RestaurantItem> mRestaurantItemItemsList;
    private final OnRestaurantListener mOnRestaurantListener;

    public ListAdapter(List<RestaurantItem> restaurantItemItemsList, OnRestaurantListener onRestaurantListener) {
        this.mRestaurantItemItemsList = restaurantItemItemsList;
        this.mOnRestaurantListener = onRestaurantListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRestaurantItems(List<RestaurantItem> restaurantItemItems) {
        this.mRestaurantItemItemsList.clear();
        this.mRestaurantItemItemsList.addAll(restaurantItemItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), mOnRestaurantListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {

        RestaurantItem restaurantItem = mRestaurantItemItemsList.get(position);

        // Setup name textview
        holder.binding.restaurantNameTextView.setText(String.valueOf(restaurantItem.getName()));

        // Setup address textview
        if (restaurantItem.getAddress() != null) {
            holder.binding.restaurantTypeAndAddressTextView.setText(String.valueOf(restaurantItem.getAddress()));
        }

        // Setup status
        boolean openStatus = restaurantItem.getIsOpen();
        if (openStatus && Objects.nonNull(openStatus)) {
            holder.binding.restaurantOpenTextView.setText(R.string.list_restaurant_open);
            holder.binding.restaurantOpenTextView.setTextColor(Color.GREEN);
        } else {
            holder.binding.restaurantOpenTextView.setText(R.string.list_restaurant_closed);
            holder.binding.restaurantOpenTextView.setTextColor(Color.RED);
        }

        // Display restaurant distance from user
        holder.binding.restaurantDistanceTextView.setText(restaurantItem.getDistance() + "m");


        // Display if someone is eating at that restaurant
        if (restaurantItem.getNumberOfPeopleEating() > 0) {
            holder.binding.peopleEatingView.setVisibility(View.VISIBLE);
            holder.binding.peopleEatingCounterView.setText(String.valueOf(restaurantItem.getNumberOfPeopleEating()));
        } else {
            holder.binding.peopleEatingView.setVisibility(View.GONE);
        }

        if (restaurantItem.getNumberOfFavorites() != 0) {
            holder.binding.likesCounterView.setText(String.valueOf(restaurantItem.getNumberOfFavorites()));
            holder.binding.likedStarImageView.setVisibility(View.VISIBLE);
        }

        // Setup ImageView
        holder.binding.restaurantImageView.setImageResource(R.drawable.drawer_image);

        // hungry in api call since we're using google places api, commented to avoid reaching daily quotas quickly
      /*if (restaurantItem.getPhoto() != null) {
           Glide.with(holder.binding.getRoot())
                    .load(restaurantItem.getPhoto())
                    .apply(RequestOptions.centerCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(holder.binding.restaurantImageView);
        } else {
            holder.binding.restaurantImageView.setImageResource(R.drawable.drawer_image);
        }*/

        holder.itemView.setOnClickListener(view -> mOnRestaurantListener.onRestaurantClick(restaurantItem.getPlaceId()));
    }

    public interface OnRestaurantListener {
        void onRestaurantClick(String placeId);
    }

    @Override
    public int getItemCount() {
        return mRestaurantItemItemsList == null ? 0 : mRestaurantItemItemsList.size();
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
