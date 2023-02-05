package com.bmathias.go4lunch_.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.data.model.RestaurantDetails;
import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.data.repositories.ConfigRepository;
import com.bmathias.go4lunch_.data.repositories.CurrentUserRepository;
import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;
import com.bmathias.go4lunch_.data.repositories.UsersRepository;

import java.util.List;


public class DetailsViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;
    private final UsersRepository usersRepository;
    private final CurrentUserRepository currentUserRepository;

    private final ConfigRepository configRepository;

    public LiveData<User> currentUser;
    private LiveData<List<User>> users;

    private final MutableLiveData<Boolean> _showProgress = new MutableLiveData<>();
    public LiveData<Boolean> showProgress = _showProgress;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    private LiveData<RestaurantDetails> restaurantDetails;

    public DetailsViewModel(RestaurantRepository restaurantRepository, CurrentUserRepository currentUserRepository, UsersRepository usersRepository, ConfigRepository configRepository) {
        this.restaurantRepository = restaurantRepository;
        this.currentUserRepository = currentUserRepository;
        this.usersRepository = usersRepository;
        this.configRepository = configRepository;
    }

    public void observeRestaurantsDetails(String placeId) {
        restaurantDetails = Transformations.map(restaurantRepository.getRestaurantDetailsObservable(placeId), result -> {
            _showProgress.postValue(false);
            if (result.isSuccess()) {
                _error.postValue(null);
                return result.getData();
            } else {
                _error.postValue(result.getError().getMessage());
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
        users = usersRepository.getUsersByPlaceId(placeId);
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

    public Boolean retrieveNotificationsPreferences(){
        return configRepository.getNotificationsPreferences();
    }
}
