package com.bmathias.go4lunch.data.repositories;

import static com.bmathias.go4lunch.utils.Constants.LIKED_RESTAURANTS;
import static com.bmathias.go4lunch.utils.Constants.USERS;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch.BuildConfig;
import com.bmathias.go4lunch.data.mappers.RestaurantMapper;
import com.bmathias.go4lunch.data.model.RestaurantDetails;
import com.bmathias.go4lunch.data.model.RestaurantItem;
import com.bmathias.go4lunch.data.model.UserLocation;
import com.bmathias.go4lunch.data.network.PlacesApiService;
import com.bmathias.go4lunch.data.network.model.DataResult;
import com.bmathias.go4lunch.data.network.model.places.RestaurantApi;
import com.bmathias.go4lunch.data.network.model.places.RestaurantsApiResult;
import com.bmathias.go4lunch.data.network.model.placesDetails.DetailsResultAPI;
import com.bmathias.go4lunch.utils.DistanceComparator;
import com.bmathias.go4lunch.utils.LocationService;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class RestaurantRepository {

    private final String photoBaseUrl;
    private final PlacesApiService placesAPIService;
    private final LocationService mLocationService;
    private final ConfigRepository configRepository;

    private final Scheduler subscribeScheduler;
    private final Scheduler observeSchedule;

    private Double latitude;
    private Double longitude;

    private static volatile RestaurantRepository instance;

    @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
    private final CurrentUserRepository currentUserRepository;

    @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
    private final CollectionReference usersRef;
    @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
    private final CollectionReference likedRestaurantsRef;

    private final UserDatasource userDatasource;

    private static final String type = "restaurant";

    private RestaurantRepository(
            LocationService locationService,
            PlacesApiService placesAPIService,
            String photoBaseUrl,
            ConfigRepository configRepository,
            FirebaseFirestore firebaseFirestore,
            CurrentUserRepository currentUserRepository,
            Scheduler subscribeScheduler,
            Scheduler observeScheduler,
            UserDatasource userDatasource) {
        this.placesAPIService = placesAPIService;
        this.photoBaseUrl = photoBaseUrl;
        this.mLocationService = locationService;
        this.configRepository = configRepository;
        this.currentUserRepository = currentUserRepository;
        this.subscribeScheduler = subscribeScheduler;
        this.observeSchedule = observeScheduler;
        usersRef = firebaseFirestore.collection(USERS);
        likedRestaurantsRef = firebaseFirestore.collection(LIKED_RESTAURANTS);
        this.userDatasource = userDatasource;
    }

    // Instance used for basic usage across the app
    public static RestaurantRepository getInstance(
            LocationService locationService,
            PlacesApiService placesAPIService,
            String photoBaseUrl,
            ConfigRepository configRepository,
            FirebaseFirestore firebaseFirestore,
            CurrentUserRepository currentUserRepository,
            Scheduler subscribeScheduler,
            Scheduler observeScheduler,
            UserDatasource userDatasource
    ) {
        RestaurantRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (RestaurantRepository.class) {
            if (instance == null) {
                instance = new RestaurantRepository(
                        locationService,
                        placesAPIService,
                        photoBaseUrl,
                        configRepository,
                        firebaseFirestore,
                        currentUserRepository,
                        subscribeScheduler,
                        observeScheduler,
                        userDatasource);
            }
            return instance;
        }
    }

    // Instance only used for testing
    public static RestaurantRepository getTestInstance(
            LocationService locationService,
            PlacesApiService placesAPIService,
            String photoBaseUrl,
            ConfigRepository configRepository,
            FirebaseFirestore firebaseFirestore,
            CurrentUserRepository currentUserRepository,
            Scheduler subscribeScheduler,
            Scheduler observeScheduler,
            UserDatasource userDatasource
    ) {
        return new RestaurantRepository(
                locationService,
                placesAPIService,
                photoBaseUrl,
                configRepository,
                firebaseFirestore,
                currentUserRepository,
                subscribeScheduler,
                observeScheduler,
                userDatasource);
    }

    // RESTAURANT PLACES API //
    // Observable that combine both sources and convert them to our own model
    private Observable<List<RestaurantItem>> getRestaurantsObservable(String query, UserLocation userLocation) {
        if (query == null) {
            query = "";
        }
        return Observable.zip(
                getRestaurantsFromApiObservable(query, userLocation),
                userDatasource.getNonDistinctSelectedRestaurantIds(),
                userDatasource.getNonDistinctFavoriteRestaurantIds(),
                (restaurantsFromApi, numberOfPeopleEating, numberOfFavorites) ->
                        RestaurantMapper.apisToItems(
                                restaurantsFromApi,
                                photoBaseUrl,
                                numberOfPeopleEating,
                                numberOfFavorites,
                                latitude,
                                longitude)
        );
    }

    private Observable<List<RestaurantApi>> getRestaurantsFromApiObservable(String query, UserLocation userLocation) {
        latitude = userLocation.getLatitude();
        longitude = userLocation.getLongitude();

        String location = latitude + "," + longitude;
        String radius = configRepository.getRadius();

        Log.d("User Location is", location);
        Log.d("Radius is", "" + radius);
        return placesAPIService.getRestaurants(query, location, radius, type, BuildConfig.MAPS_API_KEY)
                .map(RestaurantsApiResult::getResults);
    }

    // Retrieve Restaurant

    @SuppressLint("CheckResult")
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public LiveData<DataResult<List<RestaurantItem>>> getRestaurantsObservable(String query) {
        MutableLiveData<DataResult<List<RestaurantItem>>> _restaurants = new MutableLiveData<>();
        Observable<UserLocation> userLocationObservable = mLocationService.retrieveLocation();
        Log.d("User Location ", "Stream called");

        userLocationObservable.flatMap(userLocation -> getRestaurantsObservable(query, userLocation))
                .map(restaurants -> {
                    Collections.sort(restaurants, new DistanceComparator());
                    return restaurants;
                })
                .subscribeOn(subscribeScheduler)
                .observeOn(observeSchedule)
                .subscribe(restaurants -> {
                    DataResult<List<RestaurantItem>> dataResult = new DataResult<>(restaurants);
                    _restaurants.postValue(dataResult);
                }, throwable -> {
                    DataResult<List<RestaurantItem>> dataResult = new DataResult<>(throwable);
                    Log.e("onError", "======================================================");
                    Log.e("onError", "onError : " + throwable.getMessage());
                    throwable.printStackTrace();
                    Log.e("onError", "======================================================");
                    _restaurants.postValue(dataResult);
                });
        return _restaurants;
    }

    // RESTAURANT DETAILS //
    private Observable<DetailsResultAPI> getRestaurantsDetailsApiResultObservable(String placeId) {
        return placesAPIService.getRestaurantDetails(placeId, BuildConfig.MAPS_API_KEY);
    }

    // Retrieve RestaurantDetails and convert to our own model
    @SuppressLint("CheckResult")
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public LiveData<DataResult<RestaurantDetails>> getRestaurantDetailsObservable(String placeId) {

        MutableLiveData<DataResult<RestaurantDetails>> _restaurantDetails = new MutableLiveData<>();

        Observable.zip(getRestaurantsDetailsApiResultObservable(placeId), userDatasource.getCurrentUserFavoriteObservable(placeId), (detailsResultAPI, currentUserFavorite)
                        -> RestaurantMapper.apiToDetails(detailsResultAPI.getResult(), photoBaseUrl, currentUserFavorite, BuildConfig.MAPS_API_KEY)
                )
                .subscribeOn(subscribeScheduler)
                .observeOn(observeSchedule)
                .subscribe(detailsResult -> {
                    DataResult<RestaurantDetails> dataResult = new DataResult<>(detailsResult);
                    _restaurantDetails.postValue(dataResult);
                }, throwable -> {
                    DataResult<RestaurantDetails> dataResult = new DataResult<>(throwable);
                    _restaurantDetails.postValue(dataResult);
                });

        return _restaurantDetails;
    }

    // Update user selected restaurant
    public void updateSelectedRestaurant(String placeId, String placeName) {
        userDatasource.updateSelectedRestaurant(placeId, placeName);
    }

    // Delete user selected restaurant
    public void deleteSelectedRestaurant() {
        userDatasource.deleteSelectedRestaurant();
    }

    // Add a favorite restaurant
    public void addFavoriteRestaurant(String placeId) {
        userDatasource.addFavoriteRestaurant(placeId);
    }

    // Remove a specific favorite restaurant
    public void removeFavoriteRestaurant(String placeId) {
        userDatasource.removeFavoriteRestaurant(placeId);
    }

    // Commented methods for autocomplete, cannot be used in our case since the API isn't sending needed data
    // Code is still kept after review with lead dev
    // AUTOCOMPLETE //
   /* private Observable<List<AutocompletePrediction>> getRestaurantsPlaceAutoCompleteObservable(String query, UserLocation location) {

        BehaviorSubject<List<AutocompletePrediction>> autocompletePredictionsObservable = BehaviorSubject.create();

        Places.initialize(App.getContext(), BuildConfig.MAPS_API_KEY);
        PlacesClient placesClient = Places.createClient(App.getContext());
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // Specified radius
        double radius = 2.0;

        // Get rectangular bounds from location
        double[] boundsFromLatLng = getBoundsFromLatLng(radius, location.getLatitude(), location.getLongitude());

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setOrigin(new LatLng(location.getLatitude(), location.getLongitude()))
                .setLocationRestriction(RectangularBounds.newInstance(
                        new LatLng(boundsFromLatLng[0], boundsFromLatLng[1]),
                        new LatLng(boundsFromLatLng[2], boundsFromLatLng[3])))
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setSessionToken(token)
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener(response -> {
                    List<AutocompletePrediction> filteredList = new ArrayList<>();
                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        if (prediction.getPlaceTypes().contains(Place.Type.FOOD)) {
                            filteredList.add(prediction);
                        }
                    }

                    autocompletePredictionsObservable.onNext(filteredList);

                    // Log purpose
                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        Log.d("Autocomplete id : ", prediction.getPlaceId());
                        Log.d("Autocomplete text : ", prediction.getPrimaryText(null).toString());
                        Log.d("Autocomplete type : ", prediction.getPlaceTypes().toString());
                        Log.d("Autocomplete distance :", Objects.requireNonNull(prediction.getDistanceMeters()).toString());
                    }

                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.d("Autocomplete error : ", "Place not found: " + apiException.getStatusCode());
                    }
                });
        return autocompletePredictionsObservable;
    }

    // Method used to restrict the result of autocomplete to a certain radius
    public double[] getBoundsFromLatLng(double radius, double lat, double lng) {
        double lat_change = radius / 111.2f;
        double lon_change = Math.abs(Math.cos(lat * (Math.PI / 180)));
        return new double[]{
                lat - lat_change,
                lng - lon_change,
                lat + lat_change,
                lng + lon_change
        };
    }
     // Convert the Restaurant model provided by Autocomplete to our own model
    private static List<RestaurantItem> restaurantAutocompleteConverter(List<AutocompletePrediction> restaurantAutocomplete, String photoBaseUrl, List<String> eatingAtRestaurant, List<String> likedRestaurants) {
        List<RestaurantItem> restaurantsList = new ArrayList<>(restaurantAutocomplete.size());

        CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.NORMAL);

        for (AutocompletePrediction restaurantsAutocomplete : restaurantAutocomplete) {
            RestaurantItem.Builder builder = new RestaurantItem.Builder()
                    .withDistance(Objects.requireNonNull(restaurantsAutocomplete.getDistanceMeters()).floatValue())
                    .withName(restaurantsAutocomplete.getPrimaryText(STYLE_BOLD).toString())
                    .withAddress(restaurantsAutocomplete.getSecondaryText(STYLE_BOLD).toString());

            if (eatingAtRestaurant != null) {
                int numberOfPeopleEating = Collections.frequency(eatingAtRestaurant, restaurantsAutocomplete.getPlaceId());
                builder.withIsSomeoneEating(numberOfPeopleEating);
            }

            if (likedRestaurants != null) {
                int numberOfFavorites = Collections.frequency(likedRestaurants, restaurantsAutocomplete.getPlaceId());
                builder.withNumberOfFavorites(numberOfFavorites);
            }

            RestaurantItem restaurants = builder.build();
            restaurantsList.add(restaurants);
        }
        return restaurantsList;
    }
    */
}
