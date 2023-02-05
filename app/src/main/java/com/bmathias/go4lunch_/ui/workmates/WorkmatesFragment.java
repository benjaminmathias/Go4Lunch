package com.bmathias.go4lunch_.ui.workmates;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.databinding.FragmentWorkmatesBinding;
import com.bmathias.go4lunch_.injection.Injection;
import com.bmathias.go4lunch_.injection.ViewModelFactory;
import com.bmathias.go4lunch_.ui.list.DetailsActivity;
import com.bmathias.go4lunch_.viewmodel.WorkmatesViewModel;

import java.util.ArrayList;

public class WorkmatesFragment extends Fragment implements WorkmatesAdapter.OnUserListener, WorkmatesAdapter.OnUserChatListener {

    private WorkmatesViewModel workmatesViewModel;
    private WorkmatesAdapter adapter;
    private FragmentWorkmatesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);

        requireActivity().setTitle(R.string.workmates_fragment_name);
        this.setupRecyclerView();
        this.setupViewModel();
        observeLiveData();
        return binding.getRoot();
    }

    private void setupViewModel(){
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        this.workmatesViewModel = new ViewModelProvider(this, viewModelFactory).get(WorkmatesViewModel.class);
        this.adapter.setRecyclerViewWorkmatesViewModel(workmatesViewModel);
    }

    private void observeLiveData() {
        workmatesViewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
                    adapter.setUserItems(users);
            if (adapter.getItemCount() == 0) {
                binding.emptyView.getRoot().setVisibility(View.VISIBLE);
                binding.emptyView.title.setText(R.string.empty_dataset_no_result);
                binding.emptyView.subTitle.setText(R.string.empty_dataset_no_result_description);
                binding.fragmentWorkmatesRecyclerView.setVisibility(View.GONE);
            }
        });

        workmatesViewModel.error.observe(getViewLifecycleOwner(), error -> {
            binding.emptyView.getRoot().setVisibility(View.VISIBLE);
            binding.emptyView.title.setText(R.string.empty_dataset_error);
            binding.emptyView.subTitle.setText(R.string.empty_dataset_error_description);
            binding.fragmentWorkmatesRecyclerView.setVisibility(View.GONE);
            binding.progressbar.setVisibility(View.GONE);
        });

        workmatesViewModel.showProgress.observe(getViewLifecycleOwner(), isVisible -> binding.progressbar.setVisibility(isVisible ? View.VISIBLE : View.GONE));
    }

    private void setupRecyclerView(){
        adapter = new WorkmatesAdapter(new ArrayList<>(), this, this);
        adapter.setRecyclerViewWorkmatesViewModel(workmatesViewModel);

        binding.fragmentWorkmatesRecyclerView.setAdapter(adapter);
        binding.fragmentWorkmatesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onUserClick(String placeId) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra("placeId", placeId);
        startActivity(intent);
    }

    @Override
    public void onUserChatClick(String userId) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }
}
