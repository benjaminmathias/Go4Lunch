package com.bmathias.go4lunch_.ui.list;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.data.model.RestaurantItem;
import com.bmathias.go4lunch_.databinding.FragmentListItemBinding;

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
        // TODO: commented for testing purpose, remove for demo
        /*
        if (restaurant.getPhoto() != null) {
           Glide.with(holder.binding.getRoot())
                    .load(restaurant.getPhoto())
                    .apply(RequestOptions.centerCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(holder.binding.restaurantImageView);
        } else {
            holder.binding.restaurantImageView.setImageResource(R.drawable.ic_baseline_fastfood_24);
        }
*/

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
