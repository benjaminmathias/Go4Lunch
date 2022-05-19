package com.bmathias.go4lunch_.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bmathias.go4lunch_.databinding.FragmentListBinding;
import com.bmathias.go4lunch_.injection.Injection;
import com.bmathias.go4lunch_.injection.ViewModelFactory;
import com.bmathias.go4lunch_.viewmodel.ListViewModel;

import java.util.ArrayList;


public class ListFragment extends Fragment implements ListAdapter.OnRestaurantListener {

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
        adapter = new ListAdapter(new ArrayList<>(), this);

        listViewModel.getRestaurants().observe(getViewLifecycleOwner(), restaurantItems ->
                adapter.setRestaurantItems(restaurantItems));

        binding.fragmentListRecyclerView.setAdapter(adapter);
        binding.fragmentListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.fragmentListRecyclerView.addItemDecoration(new DividerItemDecoration(binding.fragmentListRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

    }

    // Handle onClick event on RecyclerView items
    @Override
    public void onRestaurantClick(String placeId) {
        Toast.makeText(this.getActivity(), "Click on " + placeId, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra("placeId", placeId);
        startActivity(intent);
    }
}
