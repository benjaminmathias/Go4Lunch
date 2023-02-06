package com.bmathias.go4lunch.viewmodel;

import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch.LiveDataTestUtil;
import com.bmathias.go4lunch.data.model.RestaurantDetails;
import com.bmathias.go4lunch.data.model.User;
import com.bmathias.go4lunch.data.network.model.DataResult;
import com.bmathias.go4lunch.data.repositories.ConfigRepository;
import com.bmathias.go4lunch.data.repositories.CurrentUserRepository;
import com.bmathias.go4lunch.data.repositories.RestaurantRepository;
import com.bmathias.go4lunch.data.repositories.UsersRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public class DetailsViewModelTest {

    @Rule
    public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();

    private DetailsViewModel detailsViewModel;

    private final RestaurantRepository restaurantRepository = Mockito.mock(RestaurantRepository.class);

    private final ConfigRepository configRepository = Mockito.mock(ConfigRepository.class);

    private final CurrentUserRepository currentUserRepository = Mockito.mock(CurrentUserRepository.class);

    private final UsersRepository usersRepository = Mockito.mock(UsersRepository.class);

    private final RestaurantDetails detailsResult = new RestaurantDetails();
    @Before
    public void setup(){
        detailsViewModel = new DetailsViewModel(restaurantRepository, currentUserRepository, usersRepository, configRepository);
    }


    @Test
    public void getRestaurantDetailsShouldReturnResultWhenRepoReturnSuccess() throws InterruptedException {
        String placeId = "123";
        DataResult<RestaurantDetails> dataResult = new DataResult<>(detailsResult);
        MutableLiveData<DataResult<RestaurantDetails>> _restaurantDetails = new MutableLiveData<>(dataResult);
        when(restaurantRepository.getRestaurantDetailsObservable(placeId)).thenReturn(_restaurantDetails);

        detailsViewModel.observeRestaurantsDetails(placeId);
        RestaurantDetails result = LiveDataTestUtil.getOrAwaitValue(detailsViewModel.getRestaurantDetails());

        Boolean progress = LiveDataTestUtil.getOrAwaitValue(detailsViewModel.showProgress);
        String error = LiveDataTestUtil.getOrAwaitValue(detailsViewModel.error);

        Assert.assertNotNull(result);
        Assert.assertNull(error);
        Assert.assertFalse(progress);
        Assert.assertEquals(result, Objects.requireNonNull(_restaurantDetails.getValue()).getData());
    }

    @Test
    public void getRestaurantDetailsShouldReturnErrorWhenRepoReturnException() throws InterruptedException {
        TimeoutException exception = new TimeoutException("I'm sorry");
        String placeId = "123";
        DataResult<RestaurantDetails> dataResult = new DataResult<>(exception);
        MutableLiveData<DataResult<RestaurantDetails>> _restaurantDetails = new MutableLiveData<>(dataResult);
        when(restaurantRepository.getRestaurantDetailsObservable(placeId)).thenReturn(_restaurantDetails);

        detailsViewModel.observeRestaurantsDetails(placeId);
        RestaurantDetails result = LiveDataTestUtil.getOrAwaitValue(detailsViewModel.getRestaurantDetails());

        Boolean progress = LiveDataTestUtil.getOrAwaitValue(detailsViewModel.showProgress);
        String error = LiveDataTestUtil.getOrAwaitValue(detailsViewModel.error);

        Assert.assertNull(result);
        Assert.assertEquals("I'm sorry", error);
        Assert.assertFalse(progress);
    }

    @Test
    public void getUserFromDatabaseShouldReturnCurrentUserWhenRepoReturnSuccess() throws InterruptedException{
        User dataUser = new User();
        dataUser.setUserId("123");
        MutableLiveData<User> authUser = new MutableLiveData<>();
        authUser.postValue(dataUser);

        when(currentUserRepository.getCurrentUser()).thenReturn(authUser);
        detailsViewModel.getUserFromDatabase();

        User user = LiveDataTestUtil.getOrAwaitValue(detailsViewModel.currentUser);

        Assert.assertEquals(user, authUser.getValue());
        Assert.assertEquals(user.getUserId(), dataUser.getUserId());
    }

    @Test
    public void getUserFromDatabaseShouldReturnNullWhenRepoReturnError() throws InterruptedException{
        MutableLiveData<User> authUser = new MutableLiveData<>();
        authUser.postValue(null);

        when(currentUserRepository.getCurrentUser()).thenReturn(authUser);
        detailsViewModel.getUserFromDatabase();

        User user = LiveDataTestUtil.getOrAwaitValue(detailsViewModel.currentUser);

        Assert.assertNull(authUser.getValue());
        Assert.assertNull(user);
    }

    @Test
    public void getSpecificUsersShouldReturnUsersWhenRepoReturnSuccess() throws InterruptedException{
        String placeId = "123";
        List<User> users = new ArrayList<>();
        LiveData<List<User>> _users = new LiveData<List<User>>(users) {};
        when(usersRepository.getUsersByPlaceId(placeId)).thenReturn(_users);
        detailsViewModel.getSpecificUsersFromDatabase(placeId);

        List<User> result = LiveDataTestUtil.getOrAwaitValue(detailsViewModel.getSpecificUsers());

        Assert.assertNotNull(result);
        Assert.assertEquals(result, _users.getValue());
    }

    @Test
    public void getSpecificUsersShouldReturnErrorWhenRepoReturnNull() throws InterruptedException{
        String placeId = "123";
        LiveData<List<User>> _users = new LiveData<List<User>>(null) {};
        when(usersRepository.getUsersByPlaceId(placeId)).thenReturn(_users);
        detailsViewModel.getSpecificUsersFromDatabase(placeId);

        List<User> result = LiveDataTestUtil.getOrAwaitValue(detailsViewModel.getSpecificUsers());


        Assert.assertNull(result);
        Assert.assertNull(_users.getValue());
    }
}
