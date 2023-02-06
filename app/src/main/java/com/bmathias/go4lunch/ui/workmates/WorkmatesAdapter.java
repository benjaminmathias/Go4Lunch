package com.bmathias.go4lunch.ui.workmates;

import static com.bmathias.go4lunch.utils.App.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmathias.go4lunch.R;
import com.bmathias.go4lunch.data.model.User;
import com.bmathias.go4lunch.databinding.FragmentWorkmatesItemsBinding;
import com.bmathias.go4lunch.viewmodel.WorkmatesViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.ViewHolder> {

    private final List<User> userItemsList;
    private final OnUserListener mOnUserListener;
    private final OnUserChatListener mOnUserChatListener;
    private final Context context = getContext();
    private WorkmatesViewModel workmatesViewModel;


    public WorkmatesAdapter(List<User> userItemsList, OnUserListener onUserListener, OnUserChatListener onUserChatListener) {
        this.userItemsList = userItemsList;
        this.mOnUserListener = onUserListener;
        this.mOnUserChatListener = onUserChatListener;
    }

    public void setRecyclerViewWorkmatesViewModel(WorkmatesViewModel workmatesViewModel) {
        this.workmatesViewModel = workmatesViewModel;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WorkmatesAdapter.ViewHolder holder, int position) {

        User user = userItemsList.get(position);

        // Setup textview
        if (user.getSelectedRestaurantId() != null) {
            holder.binding.workmatesTextview.setText(user.getUserName() + context.getResources().getString(R.string.workmates_eating_at) + user.getSelectedRestaurantName());
        } else {
            holder.binding.workmatesTextview.setText(user.getUserName() + context.getResources().getString(R.string.workmates_not_eating));
            holder.binding.workmatesTextview.setTypeface(holder.binding.workmatesTextview.getTypeface(), Typeface.ITALIC);
        }

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

        if (user.getSelectedRestaurantId() != null) {
            holder.itemView.setOnClickListener(view -> mOnUserListener.onUserClick(user.getSelectedRestaurantId()));
        }


        if (!user.getUserId().equals(workmatesViewModel.getCurrentUserId())) {
            holder.binding.workmatesChat.setOnClickListener(view -> mOnUserChatListener.onUserChatClick(user.getUserId()));
            holder.binding.workmatesChat.setImageResource(R.drawable.ic_baseline_chat_24);
        }
    }

    public interface OnUserListener {
        void onUserClick(String placeId);
    }

    public interface OnUserChatListener {
        void onUserChatClick(String message);
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
