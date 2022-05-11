package com.bmathias.go4lunch_.ui.workmates;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.databinding.FragmentWorkmatesItemsBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.ViewHolder> {

    private final List<User> userItemsList;
    private final OnUserListener mOnUserListener;

    public WorkmatesAdapter(List<User> userItemsList, OnUserListener onUserListener) {
        this.userItemsList = userItemsList;
        this.mOnUserListener = onUserListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUserItems(List<User> userItems) {
        this.userItemsList.clear();
        this.userItemsList.addAll(userItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkmatesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentWorkmatesItemsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), mOnUserListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesAdapter.ViewHolder holder, int position) {

        User user = userItemsList.get(position);

        // Setup textview
        holder.binding.workmatesTextview.setText(user.getUserName() + " is eating at " + user.getSelectedRestaurantName());

        // Setup imageview

        if (user.getPhotoUrl() != null) {
            Glide.with(holder.binding.getRoot())
                    .load(user.getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(holder.binding.workmatesImage);
        } else {
            holder.binding.workmatesImage.setImageResource(R.drawable.ic_baseline_fastfood_24);
        }

        holder.itemView.setOnClickListener(view -> mOnUserListener.onUserClick(user.getSelectedRestaurantId()));

    }

    public interface OnUserListener {
        void onUserClick(String placeId);
    }

    @Override
    public int getItemCount() {
        return userItemsList == null ? 0 : userItemsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final FragmentWorkmatesItemsBinding binding;
        OnUserListener onUserListener;

        public ViewHolder(FragmentWorkmatesItemsBinding binding, OnUserListener onUserListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onUserListener = onUserListener;
        }
    }
}
