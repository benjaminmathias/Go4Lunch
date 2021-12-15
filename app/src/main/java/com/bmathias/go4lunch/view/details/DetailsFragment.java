package com.bmathias.go4lunch.view.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bmathias.go4lunch.network.model.RestaurantPlaceDetailsResponse.RestaurantDetailsAPI;
import com.bmathias.go4lunch.databinding.FragmentDetailsBinding;

import io.reactivex.disposables.Disposable;

public class DetailsFragment extends Fragment {

    private FragmentDetailsBinding binding;
    private Disposable disposable;
    private RestaurantDetailsAPI restaurantDetailsAPI;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        this.restaurantDetailsAPI = new RestaurantDetailsAPI();

       // executeHttpRequestWithRetrofit();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }
/*
    private void executeHttpRequestWithRetrofit() {


        String url = getArguments().getString("placeId");
        String api_key = "&key=AIzaSyDKVEFMvGvHtXCQeWzF1_xYjVDHLuikiCE";

        this.disposable = PlaceStreams.streamFetchRestaurantDetails(url, api_key).subscribeWith(new DisposableObserver<DetailsResultAPI>() {

            @Override
            public void onNext(DetailsResultAPI restaurantDetails) {
                Log.e("TAG", "On Next");
                RestaurantDetailsAPI restaurant = restaurantDetails.getResult();
                // Update UI with list of restaurants
                updateUI(restaurant);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "On Error" + Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e("TAG", "On Complete Details!!");
            }

        });
    }
*/
    private void disposeWhenDestroy() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    private void updateUI(RestaurantDetailsAPI restaurant) {
      /*  binding.restaurantDetailsPicture.setImageResource(R.drawable.ic_lunch);
        binding.restaurantDetailsAddress.setText(restaurant.getFormattedAddress());
        binding.restaurantDetailsName.setText(restaurant.getName());
        binding.restaurantDetailsPhone.setText("Phone");
        binding.restaurantDetailsLike.setText("Like");
        binding.restaurantDetailsWebsite.setText("Website");*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
