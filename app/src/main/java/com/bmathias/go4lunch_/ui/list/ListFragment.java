package com.bmathias.go4lunch_.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;

import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.databinding.FragmentListBinding;
import com.bmathias.go4lunch_.injection.Injection;
import com.bmathias.go4lunch_.injection.ViewModelFactory;
import com.bmathias.go4lunch_.viewmodel.ListViewModel;

import java.util.ArrayList;

public class ListFragment extends Fragment implements ListAdapter.OnRestaurantListener {

    private ListViewModel listViewModel;
    private ListAdapter adapter;
    private FragmentListBinding binding;

    private final Handler mHandler = new Handler();

    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true);
        requireActivity().setTitle(R.string.list_fragment_name);
        this.setupRecyclerView();
        this.setupViewModel();
        observeLiveData();
        loadRestaurants(null);

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.list_search_hint));
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadRestaurants(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
              /*  mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(() -> {
                    loadRestaurants(newText);
                    Log.d("onQueryTextChange", "New autocomplete request !");
                }, 1000);*/
                return true;
            }
        });

        menuItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.search) {
            return true;
        }
        return false;
    }

    private void setupViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        this.listViewModel = new ViewModelProvider(this, viewModelFactory).get(ListViewModel.class);
    }

    private void loadRestaurants(String query) {
        this.listViewModel.loadRestaurants(query).observe(getViewLifecycleOwner(), aBoolean -> {

        });
    }

    private void observeLiveData() {
        listViewModel.getRestaurants().observe(getViewLifecycleOwner(), restaurants -> {
            adapter.setRestaurantItems(restaurants);
            if (adapter.getItemCount() == 0) {
                binding.emptyView.getRoot().setVisibility(View.VISIBLE);
                binding.emptyView.title.setText(R.string.empty_dataset_no_result);
                binding.emptyView.subTitle.setText(R.string.empty_dataset_no_result_description);
                binding.fragmentListRecyclerView.setVisibility(View.GONE);
            }
        });

        listViewModel.error.observe(getViewLifecycleOwner(), error -> {
            binding.emptyView.getRoot().setVisibility(View.VISIBLE);
            binding.emptyView.title.setText(R.string.empty_dataset_error);
            binding.emptyView.subTitle.setText(R.string.empty_dataset_error_description);
            binding.fragmentListRecyclerView.setVisibility(View.GONE);
            binding.progressbar.setVisibility(View.GONE);
        });

        listViewModel.showProgress.observe(getViewLifecycleOwner(), isVisible -> binding.progressbar.setVisibility(isVisible ? View.VISIBLE : View.GONE));
    }

    private void setupRecyclerView() {
        adapter = new ListAdapter(new ArrayList<>(), this);

        binding.fragmentListRecyclerView.setAdapter(adapter);
        binding.fragmentListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.fragmentListRecyclerView.addItemDecoration(new DividerItemDecoration(binding.fragmentListRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

    }

    // Handle onClick event on RecyclerView items
    @Override
    public void onRestaurantClick(String placeId) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra("placeId", placeId);
        startActivity(intent);
    }
}
