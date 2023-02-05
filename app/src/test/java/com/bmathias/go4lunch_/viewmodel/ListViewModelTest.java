package com.bmathias.go4lunch_.viewmodel;

import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch_.LiveDataTestUtil;
import com.bmathias.go4lunch_.data.model.RestaurantItem;
import com.bmathias.go4lunch_.data.network.model.DataResult;
import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public class ListViewModelTest {

    @Rule
    public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();

    private ListViewModel listViewModel;

    private final RestaurantRepository restaurantRepository = Mockito.mock(RestaurantRepository.class);

    @Before
    public void setup() {
        listViewModel = new ListViewModel(restaurantRepository);
    }

    @Test
    public void getRestaurantsShouldReturnResultWhenRepoReturnSuccess() throws InterruptedException {
        DataResult<List<RestaurantItem>> dataResult = new DataResult<>(new ArrayList<>());
        MutableLiveData<DataResult<List<RestaurantItem>>> _restaurants = new MutableLiveData<>(dataResult);
        when(restaurantRepository.getRestaurantsObservable(null)).thenReturn(_restaurants);

        List<RestaurantItem> result = LiveDataTestUtil.getOrAwaitValue(listViewModel.getRestaurants(null));

        Boolean progress = LiveDataTestUtil.getOrAwaitValue(listViewModel.showProgress);
        String error = LiveDataTestUtil.getOrAwaitValue(listViewModel.error);

        Assert.assertNotNull(result);
        Assert.assertNull(error);
        Assert.assertEquals(result, Objects.requireNonNull(_restaurants.getValue()).getData());
        Assert.assertFalse(progress);
    }

    @Test
    public void getRestaurantsShouldReturnErrorWhenRepoReturnException() throws InterruptedException {
        TimeoutException exception = new TimeoutException("I'm sorry");
        DataResult<List<RestaurantItem>> dataResult = new DataResult<>(exception);
        MutableLiveData<DataResult<List<RestaurantItem>>> _restaurants = new MutableLiveData<>(dataResult);
        when(restaurantRepository.getRestaurantsObservable(null)).thenReturn(_restaurants);

        List<RestaurantItem> result = LiveDataTestUtil.getOrAwaitValue(listViewModel.getRestaurants(null));

        Boolean progress = LiveDataTestUtil.getOrAwaitValue(listViewModel.showProgress);
        String error = LiveDataTestUtil.getOrAwaitValue(listViewModel.error);

        Assert.assertEquals(result, Collections.emptyList());
        Assert.assertEquals("I'm sorry", error);
        Assert.assertFalse(progress);
    }
}
