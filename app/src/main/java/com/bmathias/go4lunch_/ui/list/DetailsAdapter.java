package com.bmathias.go4lunch_.ui.list;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.databinding.ActivityDetailsBinding;
import com.bmathias.go4lunch_.databinding.ActivityDetailsItemBinding;
import com.bmathias.go4lunch_.databinding.FragmentWorkmatesItemsBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

   private final List<User> specificUsersItemsList;

   public DetailsAdapter(List<User> userItemsList) {
      this.specificUsersItemsList = userItemsList;
   }

   @SuppressLint("NotifyDataSetChanged")
   public void setUserItems(List<User> userItems) {
      this.specificUsersItemsList.clear();
      this.specificUsersItemsList.addAll(userItems);
      notifyDataSetChanged();
   }

   @NonNull
   @Override
   public DetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new DetailsAdapter.ViewHolder(ActivityDetailsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
   }

   @Override
   public void onBindViewHolder(@NonNull DetailsAdapter.ViewHolder holder, int position) {

      User user = specificUsersItemsList.get(position);

      // Setup textview
      holder.binding.detailsListTextview.setText(user.getUserName() + " is joining at " + user.getSelectedRestaurantName() + " !");

      // Setup imageview
      if (user.getPhotoUrl() != null) {
         Glide.with(holder.binding.getRoot())
                 .load(user.getPhotoUrl())
                 .apply(RequestOptions.circleCropTransform())
                 .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                 .into(holder.binding.detailsListImage);
      } else {
         holder.binding.detailsListImage.setImageResource(R.drawable.ic_baseline_fastfood_24);
      }
   }

   @Override
   public int getItemCount() {
      return specificUsersItemsList == null ? 0 : specificUsersItemsList.size();
   }

   public static class ViewHolder extends RecyclerView.ViewHolder {
      private final ActivityDetailsItemBinding binding;

      public ViewHolder(ActivityDetailsItemBinding binding) {
         super(binding.getRoot());
         this.binding = binding;
      }
   }
}
