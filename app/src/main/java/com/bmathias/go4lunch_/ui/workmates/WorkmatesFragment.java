package com.bmathias.go4lunch_.ui.workmates;

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

import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.databinding.FragmentWorkmatesBinding;
import com.bmathias.go4lunch_.injection.Injection;
import com.bmathias.go4lunch_.injection.ViewModelFactory;
import com.bmathias.go4lunch_.ui.list.DetailsActivity;
import com.bmathias.go4lunch_.viewmodel.WorkmatesViewModel;

import java.util.ArrayList;

public class WorkmatesFragment extends Fragment implements WorkmatesAdapter.OnUserListener {

    private WorkmatesViewModel workmatesViewModel;
    private WorkmatesAdapter adapter;
    private FragmentWorkmatesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);

        requireActivity().setTitle(R.string.workmates_fragment_name);
        this.setupViewModel();
        this.setupRecyclerView();
        return binding.getRoot();

    }

    private void setupViewModel(){
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        this.workmatesViewModel = new ViewModelProvider(this, viewModelFactory).get(WorkmatesViewModel.class);
        this.workmatesViewModel.getUsersFromDatabase();
    }

    private void setupRecyclerView(){
        adapter = new WorkmatesAdapter(new ArrayList<>(), this);

        this.workmatesViewModel.getUsers().observe(getViewLifecycleOwner(), users ->
                adapter.setUserItems(users));

        binding.fragmentWorkmatesRecyclerView.setAdapter(adapter);
        binding.fragmentWorkmatesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.fragmentWorkmatesRecyclerView.addItemDecoration(new DividerItemDecoration(binding.fragmentWorkmatesRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

    }

    @Override
    public void onUserClick(String placeId) {
        Toast.makeText(this.getActivity(), "Click on " + placeId, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra("placeId", placeId);
        startActivity(intent);
    }
}
