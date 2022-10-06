package com.bmathias.go4lunch_.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.data.model.RestaurantDetails;
import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.data.repositories.CurrentUserRepository;
import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;
import com.bmathias.go4lunch_.data.repositories.UsersRepository;

import java.util.List;


public class DetailsViewModel extends ViewModel {

    private static final String TAG = "DetailsViewModel";

    private final RestaurantRepository restaurantRepository;

    private final UsersRepository usersRepository;

    private final CurrentUserRepository currentUserRepository;

    public LiveData<User> currentUser;

    private LiveData<List<User>> users;

    private final MutableLiveData<Boolean> _showProgress = new MutableLiveData<>();
    public LiveData<Boolean> showProgress = _showProgress;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    private LiveData<RestaurantDetails> restaurantDetails;

    public DetailsViewModel(RestaurantRepository restaurantRepository, CurrentUserRepository currentUserRepository, UsersRepository usersRepository) {
        this.restaurantRepository = restaurantRepository;
        this.currentUserRepository = currentUserRepository;
        this.usersRepository = usersRepository;
    }

    public void observeRestaurantDetails(String placeId) {
        restaurantDetails = Transformations.map(restaurantRepository.getRestaurantDetailsObservable(placeId), result -> {
            _showProgress.postValue(false);

            if (result.isSuccess()) {
                return result.getData();
            } else {
               // _error.postValue(result.getError().getMessage());
                return null;
            }
        });
    }

    public LiveData<RestaurantDetails> getRestaurantDetails() {
        return restaurantDetails;
    }

    public void getUserFromDatabase(){
        currentUser = currentUserRepository.getCurrentUser();
    }

    public void getSpecificUsersFromDatabase(String placeId){
        LiveData<List<User>> specificUsers = usersRepository.retrieveSpecificEatingUsers(placeId);
        users = specificUsers;
    }

    public LiveData<List<User>> getSpecificUsers(){
        return users;
    }

    public void updateSelectedRestaurant(String placeId, String placeName){
        restaurantRepository.updateSelectedRestaurant(placeId, placeName);
    }

    public void deleteSelectedRestaurant(){
        restaurantRepository.deleteSelectedRestaurant();
    }

    public void addFavoriteRestaurant(String placeId){
        restaurantRepository.addFavoriteRestaurant(placeId);
    }

    public void deleteFavoriteRestaurant(String placeId){
        restaurantRepository.removeFavoriteRestaurant(placeId);
    }
}
