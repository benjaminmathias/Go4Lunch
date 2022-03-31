package com.bmathias.go4lunch_.viewmodel;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.data.network.model.DataResult;
import com.bmathias.go4lunch_.data.network.model.places.RestaurantApi;
import com.bmathias.go4lunch_.data.network.model.placesDetails.RestaurantDetailsAPI;
import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;
import com.bmathias.go4lunch_.databinding.ActivityDetailsBinding;
import com.bmathias.go4lunch_.ui.list.DetailsActivity;

import java.util.List;
import java.util.Objects;


public class DetailsViewModel extends ViewModel {

    private static final String TAG = "DetailsViewModel";

    private final RestaurantRepository restaurantRepository;

    private final MutableLiveData<Boolean> _showProgress = new MutableLiveData<>();
    public LiveData<Boolean> showProgress = _showProgress;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    private LiveData<RestaurantDetailsAPI> restaurantDetails;

    public DetailsViewModel(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
        observeRestaurantDetails(null);
    }

    public void observeRestaurantDetails(String placeId) {
        LiveData<RestaurantDetailsAPI> _restaurantDetails = Transformations.map(restaurantRepository.streamFetchRestaurantDetails(placeId), result -> {
            _showProgress.postValue(false);

            if (result.isSuccess()) {
                return result.getData();
            } else {
             //   _error.postValue(result.getError().getMessage());
                return null;
            }
        });

        restaurantDetails = _restaurantDetails;
    }

    public LiveData<RestaurantDetailsAPI> getRestaurantDetails() {
        return restaurantDetails;
    }
}
