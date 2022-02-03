package com.bmathias.go4lunch_.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bmathias.go4lunch_.data.network.model.places.RestaurantApi;
import com.bmathias.go4lunch_.injection.Injection;
import com.bmathias.go4lunch_.injection.ViewModelFactory;
import com.bmathias.go4lunch_.viewmodel.ListViewModel;
import com.bmathias.go4lunch_.databinding.FragmentListBinding;

import java.util.List;


public class ListFragment extends Fragment {

    private ListViewModel listViewModel;
    private ListAdapter adapter;
    private FragmentListBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);

        this.setupViewModel();
        this.setupRecyclerView();
        return binding.getRoot();
    }

    private void setupViewModel(){
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        this.listViewModel = new ViewModelProvider(this, viewModelFactory).get(ListViewModel.class);
        this.listViewModel.observeRestaurants();
    }

    private void setupRecyclerView(){
        listViewModel.getRestaurants().observe(getViewLifecycleOwner(), restaurantItems -> {
            binding.fragmentListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new ListAdapter(restaurantItems);
            binding.fragmentListRecyclerView.setAdapter(adapter);
        });
    }

}
