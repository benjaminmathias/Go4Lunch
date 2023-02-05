package com.bmathias.go4lunch_.data.repositories;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.bmathias.go4lunch_.LiveDataTestUtil;
import com.bmathias.go4lunch_.data.model.RestaurantDetails;
import com.bmathias.go4lunch_.data.model.RestaurantItem;
import com.bmathias.go4lunch_.data.model.UserLocation;
import com.bmathias.go4lunch_.data.network.PlacesApiService;
import com.bmathias.go4lunch_.data.network.model.DataResult;
import com.bmathias.go4lunch_.data.network.model.places.RestaurantsApiResult;
import com.bmathias.go4lunch_.data.network.model.placesDetails.DetailsResultAPI;
import com.bmathias.go4lunch_.data.network.model.placesDetails.RestaurantDetailsApiModel;
import com.bmathias.go4lunch_.utils.LocationService;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class RestaurantRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final LocationService locationService = Mockito.mock(LocationService.class);
    private final PlacesApiService placesApiService = Mockito.mock(PlacesApiService.class);
    private final ConfigRepository configRepository = new FakeConfigRepository();
    private final FirebaseFirestore firebaseFirestore = Mockito.mock(FirebaseFirestore.class);
    private final CurrentUserRepository currentUserRepository = Mockito.mock(CurrentUserRepository.class);
    private final UserDatasource userDatasource = Mockito.mock(UserDatasource.class);

    Scheduler scheduler = Schedulers.trampoline();
    private RestaurantRepository restaurantRepo;

    @Before
    public void setup() {
        String photoBaseUrl = "photo-base-url";
        restaurantRepo = RestaurantRepository.getTestInstance(
                locationService,
                placesApiService,
                photoBaseUrl,
                configRepository,
                firebaseFirestore,
                currentUserRepository,
                scheduler,
                scheduler,
                userDatasource
        );
    }


    // Restaurant API
    @Test
    public void getRestaurantsShouldReturnErrorWhenGetRestaurantsFailed() throws InterruptedException {
        Observable<UserLocation> locationObservable = Observable.just(new UserLocation(3.0, 0.0));
        when(locationService.retrieveLocation()).thenReturn(locationObservable);
        when(userDatasource.getNonDistinctSelectedRestaurantIds()).thenReturn(Observable.just(Collections.emptyList()));
        when(userDatasource.getNonDistinctFavoriteRestaurantIds()).thenReturn(Observable.just(Collections.emptyList()));
        when(placesApiService.getRestaurants(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Observable.error(new TimeoutException("I'm sorry")));

        DataResult<List<RestaurantItem>> dataResult = LiveDataTestUtil.getOrAwaitValue(restaurantRepo.getRestaurantsObservable(""));

        Assert.assertFalse(dataResult.isSuccess());
        Assert.assertTrue(dataResult.getError() instanceof TimeoutException);
        Assert.assertEquals("I'm sorry", dataResult.getError().getMessage());
    }

    @Test
    public void getRestaurantsShouldReturnErrorWhenSelectedRestaurantRetrievalFailed() throws InterruptedException {
        Observable<UserLocation> locationObservable = Observable.just(new UserLocation(3.0, 0.0));
        when(locationService.retrieveLocation()).thenReturn(locationObservable);
        when(userDatasource.getNonDistinctSelectedRestaurantIds()).thenReturn(Observable.error(new TimeoutException("Distinct Selected Restaurant error")));
        when(userDatasource.getNonDistinctFavoriteRestaurantIds()).thenReturn(Observable.just(Collections.emptyList()));
        RestaurantsApiResult restaurantsApiResult = new RestaurantsApiResult();
        restaurantsApiResult.setResults(Collections.emptyList());
        when(placesApiService.getRestaurants(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(restaurantsApiResult));

        DataResult<List<RestaurantItem>> dataResult = LiveDataTestUtil.getOrAwaitValue(restaurantRepo.getRestaurantsObservable(""));

        Assert.assertFalse(dataResult.isSuccess());
        Assert.assertTrue(dataResult.getError() instanceof TimeoutException);
        Assert.assertEquals("Distinct Selected Restaurant error", dataResult.getError().getMessage());
    }

    @Test
    public void getRestaurantsShouldReturnErrorWhenFavoriteRestaurantRetrievalFailed() throws InterruptedException {
        Observable<UserLocation> locationObservable = Observable.just(new UserLocation(3.0, 0.0));
        when(locationService.retrieveLocation()).thenReturn(locationObservable);
        when(userDatasource.getNonDistinctSelectedRestaurantIds()).thenReturn(Observable.just(Collections.emptyList()));
        when(userDatasource.getNonDistinctFavoriteRestaurantIds()).thenReturn(Observable.error(new TimeoutException("Distinct Favorite Restaurant error")));
        RestaurantsApiResult restaurantsApiResult = new RestaurantsApiResult();
        restaurantsApiResult.setResults(Collections.emptyList());
        when(placesApiService.getRestaurants(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(restaurantsApiResult));

        DataResult<List<RestaurantItem>> dataResult = LiveDataTestUtil.getOrAwaitValue(restaurantRepo.getRestaurantsObservable(""));

        Assert.assertFalse(dataResult.isSuccess());
        Assert.assertTrue(dataResult.getError() instanceof TimeoutException);
        Assert.assertEquals("Distinct Favorite Restaurant error", dataResult.getError().getMessage());
    }

    @Test
    public void getRestaurantsShouldReturnErrorWhenLocationRetrievalFailed() throws InterruptedException {
        Observable<UserLocation> locationObservable = Observable.error(new TimeoutException("Time out"));
        when(locationService.retrieveLocation()).thenReturn(locationObservable);

        DataResult<List<RestaurantItem>> dataResult = LiveDataTestUtil.getOrAwaitValue(restaurantRepo.getRestaurantsObservable(""));

        Assert.assertFalse(dataResult.isSuccess());
        Assert.assertTrue(dataResult.getError() instanceof TimeoutException);
        Assert.assertEquals("Time out", dataResult.getError().getMessage());
    }

    @Test
    public void getRestaurantsShouldReturnSuccessWhenNoFail() throws InterruptedException {
        Observable<UserLocation> locationObservable = Observable.just(new UserLocation(3.0, 0.0));
        when(locationService.retrieveLocation()).thenReturn(locationObservable);
        when(userDatasource.getNonDistinctSelectedRestaurantIds()).thenReturn(Observable.just(Collections.emptyList()));
        when(userDatasource.getNonDistinctFavoriteRestaurantIds()).thenReturn(Observable.just(Collections.emptyList()));
        RestaurantsApiResult restaurantsApiResult = new RestaurantsApiResult();
        restaurantsApiResult.setResults(Collections.emptyList());
        when(placesApiService.getRestaurants(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(restaurantsApiResult));

        DataResult<List<RestaurantItem>> dataResult = LiveDataTestUtil.getOrAwaitValue(restaurantRepo.getRestaurantsObservable(""));

        Assert.assertTrue(dataResult.isSuccess());
    }

    // Restaurant Details API
    @Test
    public void getRestaurantDetailsShouldReturnSuccessWhenNoFail() throws InterruptedException {
        when(userDatasource.getCurrentUserFavoriteObservable(anyString())).thenReturn(Observable.just(false));
        DetailsResultAPI detailsApiResult = new DetailsResultAPI();
        detailsApiResult.setResult(new RestaurantDetailsApiModel());
        when(placesApiService.getRestaurantDetails(anyString(), anyString()))
                .thenReturn(Observable.just(detailsApiResult));

        DataResult<RestaurantDetails> dataResult = LiveDataTestUtil.getOrAwaitValue(restaurantRepo.getRestaurantDetailsObservable("123456"));

        Assert.assertTrue(dataResult.isSuccess());
    }

    @Test
    public void getRestaurantDetailsShouldReturnErrorWhenGetRestaurantDetailsFailed() throws InterruptedException {
        when(userDatasource.getCurrentUserFavoriteObservable(anyString())).thenReturn(Observable.just(false));
        when(placesApiService.getRestaurantDetails(anyString(), anyString()))
                .thenReturn(Observable.error(new TimeoutException("I'm sorry")));

        DataResult<RestaurantDetails> dataResult = LiveDataTestUtil.getOrAwaitValue(restaurantRepo.getRestaurantDetailsObservable("123456"));

        Assert.assertFalse(dataResult.isSuccess());
        Assert.assertTrue(dataResult.getError() instanceof TimeoutException);
        Assert.assertEquals("I'm sorry", dataResult.getError().getMessage());
    }

    @Test
    public void getRestaurantDetailsShouldReturnErrorWhenGetCurrentUserFavoriteFailed() throws InterruptedException {
        Observable<Boolean> userDatasourceObservable = Observable.error(new TimeoutException("Time out"));
        when(userDatasource.getCurrentUserFavoriteObservable(anyString())).thenReturn(userDatasourceObservable);
        DetailsResultAPI detailsApiResult = new DetailsResultAPI();
        detailsApiResult.setResult(new RestaurantDetailsApiModel());
        when(placesApiService.getRestaurantDetails(anyString(), anyString()))
                .thenReturn(Observable.just(detailsApiResult));

        DataResult<RestaurantDetails> dataResult = LiveDataTestUtil.getOrAwaitValue(restaurantRepo.getRestaurantDetailsObservable("123456"));

        Assert.assertFalse(dataResult.isSuccess());
        Assert.assertTrue(dataResult.getError() instanceof TimeoutException);
        Assert.assertEquals("Time out", dataResult.getError().getMessage());
    }
}