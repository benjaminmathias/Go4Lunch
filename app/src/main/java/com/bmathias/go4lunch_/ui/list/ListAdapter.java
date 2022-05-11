package com.bmathias.go4lunch_.ui.list;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.data.model.RestaurantItem;
import com.bmathias.go4lunch_.data.network.model.places.OpeningHours;
import com.bmathias.go4lunch_.databinding.FragmentListItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final List<RestaurantItem> restaurantItemsList;
    private final OnRestaurantListener mOnRestaurantListener;

    public ListAdapter(List<RestaurantItem> restaurantItemsList, OnRestaurantListener onRestaurantListener) {
        this.restaurantItemsList = restaurantItemsList;
        this.mOnRestaurantListener = onRestaurantListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRestaurantItems(List<RestaurantItem> restaurantItems){
        this.restaurantItemsList.clear();
        this.restaurantItemsList.addAll(restaurantItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), mOnRestaurantListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {

        RestaurantItem restaurant = restaurantItemsList.get(position);

        // Setup name textview
        holder.binding.restaurantNameTextView.setText(String.valueOf(restaurant.getName()));

        // Setup address textview
        holder.binding.restaurantTypeAndAddressTextView.setText(String.valueOf(restaurant.getAddress()));

        // Setup status
        OpeningHours openStatus = restaurant.getIsOpen();
        if (openStatus != null && openStatus.getOpenNow()) {
            holder.binding.restaurantOpenTextView.setText("Ouvert");
            holder.binding.restaurantOpenTextView.setTextColor(Color.GREEN);
        } else {
            holder.binding.restaurantOpenTextView.setText("Fermé");
            holder.binding.restaurantOpenTextView.setTextColor(Color.RED);
        }

        // Setup ImageView

        // For testing purpose
        holder.binding.restaurantImageView.setImageResource(R.drawable.drawer_image);

        holder.itemView.setOnClickListener(view -> mOnRestaurantListener.onRestaurantClick(restaurant.getPlaceId()));

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
