package com.bmathias.go4lunch.view.list;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmathias.go4lunch.data.model.Restaurant;
import com.bmathias.go4lunch.databinding.FragmentListBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ListFragment extends Fragment {

    private RestaurantViewModel restaurantViewModel;

    private ArrayList<Restaurant> restaurantItemsList;
    private RestaurantListAdapter adapter;

    private FragmentListBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

        initRecyclerView();
        observeData();
        restaurantViewModel.getRestaurants();

    }

    private void observeData(){
        restaurantViewModel.getRestaurantList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Restaurant>>() {
            @Override
            public void onChanged(ArrayList<Restaurant> restaurants) {
                Log.e(TAG, "onChanged:" + restaurants.size());
                adapter.updateRestaurants(restaurants);
            }
        });
    }

    private void initRecyclerView(){
        binding.fragmentRestaurantlistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RestaurantListAdapter(restaurantItemsList, getActivity(), Glide.with(this));
        binding.fragmentRestaurantlistRecyclerView.setAdapter(adapter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
       // this.disposeWhenDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    /*

    private void executeHttpRequestWithRetrofit() {
        this.disposable = RestaurantsRepository.streamFetchRestaurants().subscribeWith(new DisposableObserver<ResultsAPI>() {

            @Override
            public void onNext(ResultsAPI response) {
                Log.e("TAG", "On Next");
                List<RestaurantAPI> restaurants = response.getResults();
                // Update UI with list of restaurants
                updateUI(restaurants);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "On Error" + Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e("TAG", "On Complete !!");
            }

        });
    }

    private void disposeWhenDestroy() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

     */
/*
    private void updateUI(List<RestaurantAPI> restaurants) {
        restaurantItemsList.clear();
        restaurantItemsList.addAll(restaurants);
        adapter.notifyDataSetChanged();
    }
*/


}