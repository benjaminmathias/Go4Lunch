package com.bmathias.go4lunch.view.list;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmathias.go4lunch.data.model.Restaurant;
import com.bmathias.go4lunch.network.model.RestaurantPlaceResponse.RestaurantAPI;
import com.bmathias.go4lunch.network.model.RestaurantPlaceResponse.ResultsAPI;
import com.bmathias.go4lunch.databinding.FragmentListBinding;
import com.bmathias.go4lunch.data.repositories.RestaurantsRepository;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.WithFragmentBindings;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
@AndroidEntryPoint
public class ListFragment extends Fragment {

    private RestaurantListViewModel restaurantViewModel;

    private ArrayList<Restaurant> restaurantItemsList;
    private RestaurantListAdapter adapter;

    private FragmentListBinding binding;

    // Declare Subscription
    private Disposable disposable;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false);

       // recyclerView = binding.fragmentRestaurantlistRecyclerView;
        //    this.restaurantItemsList = new ArrayList<>();
/*
        this.adapter = new RestaurantListAdapter(this.restaurantItemsList, this.getActivity(), Glide.with(this));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
*/
       // this.executeHttpRequestWithRetrofit();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restaurantViewModel = new ViewModelProvider(this).get(RestaurantListViewModel.class);

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
        adapter = new RestaurantListAdapter(this.restaurantItemsList, this.getActivity(), Glide.with(this));
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