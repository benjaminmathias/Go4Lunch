package com.bmathias.go4lunch.data.mappers;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.bmathias.go4lunch.BuildConfig;
import com.bmathias.go4lunch.data.model.RestaurantDetails;
import com.bmathias.go4lunch.data.model.RestaurantItem;
import com.bmathias.go4lunch.data.network.model.places.Geometry;
import com.bmathias.go4lunch.data.network.model.places.Location;
import com.bmathias.go4lunch.data.network.model.places.OpeningHours;
import com.bmathias.go4lunch.data.network.model.places.Photo;
import com.bmathias.go4lunch.data.network.model.places.RestaurantApi;
import com.bmathias.go4lunch.data.network.model.placesDetails.RestaurantDetailsApiModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMapperTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final RestaurantApi restaurantApiBase1 = new RestaurantApi();
    private final RestaurantApi restaurantApiBase2 = new RestaurantApi();


    private final List<Photo> photoPlaces = new ArrayList<>();

    private final List<com.bmathias.go4lunch.data.network.model.placesDetails.Photo> photoPlacesDetails = new ArrayList<>();
    private final String photoReference = "testphoto";

    private final Geometry geometry = new Geometry();

    private final Location testLocation = new Location();

    private final Double userLatitude = 0.0;
    private final Double userLongitude = 0.0;

    private final OpeningHours openingHours = new OpeningHours();
    private final String photoBaseUrl = BuildConfig.PHOTO_BASE_URL;
    private final ArrayList<RestaurantApi> restaurantApiList = new ArrayList<>();

    private final RestaurantDetailsApiModel restaurantDetailsApiModel1 = new RestaurantDetailsApiModel();

    private final List<String> eatingAt = new ArrayList<>();
    private final List<String> likedRestaurants = new ArrayList<>();


    // Setup multiple model used to be fed to our converter
    @Before
    public void setUp() {
        // setup for restaurantApi
        geometry.setLocation(testLocation);

        Photo photo = new Photo();
        photo.setPhotoReference(photoReference);
        photoPlaces.add(photo);

        Double restaurantLatitude = 0.0;
        testLocation.setLat(restaurantLatitude);
        Double restaurantLongitude = 0.0;
        testLocation.setLng(restaurantLongitude);

        openingHours.setOpenNow(false);

        // restaurantApi
        restaurantApiBase1.setName("test1name");
        restaurantApiBase1.setPlaceId("test1placeid");
        restaurantApiBase1.setVicinity("test1vicinity");
        restaurantApiBase1.setGeometry(geometry);
        restaurantApiBase1.setOpeningHours(openingHours);
        restaurantApiBase1.setPhotos(photoPlaces);

        restaurantApiBase2.setName("test2name");
        restaurantApiBase2.setPlaceId("test2placeid");
        restaurantApiBase2.setVicinity("test2vicinity");
        restaurantApiBase2.setGeometry(geometry);
        restaurantApiBase2.setOpeningHours(openingHours);
        restaurantApiBase2.setPhotos(photoPlaces);

        restaurantApiList.add(restaurantApiBase1);
        restaurantApiList.add(restaurantApiBase2);

    }

    @Test
    public void convertRestaurantApiListToRestaurantItemListSuccess() {
        List<RestaurantItem> listConverted =
                RestaurantMapper.apisToItems(restaurantApiList, photoBaseUrl, eatingAt, likedRestaurants, userLatitude, userLongitude);

        Assert.assertEquals("test1name", listConverted.get(0).getName());
        Assert.assertEquals("test1placeid", listConverted.get(0).getPlaceId());
        Assert.assertEquals("test1vicinity", listConverted.get(0).getAddress());
        Assert.assertEquals(geometry.getLocation().getLat(), listConverted.get(0).getLatitude());
        Assert.assertEquals(geometry.getLocation().getLng(), listConverted.get(0).getLongitude());
        Assert.assertFalse(listConverted.get(0).getIsOpen());
        Assert.assertTrue(photoPlaces.get(0).getPhotoReference(), listConverted.get(0).getPhoto().contains("testphoto"));
        Assert.assertEquals(0, listConverted.get(0).getNumberOfPeopleEating());
        Assert.assertEquals(0, listConverted.get(0).getNumberOfFavorites());

        Assert.assertEquals("test2name", listConverted.get(1).getName());
        Assert.assertEquals("test2placeid", listConverted.get(1).getPlaceId());
        Assert.assertEquals("test2vicinity", listConverted.get(1).getAddress());
        Assert.assertEquals(geometry.getLocation().getLat(), listConverted.get(1).getLatitude());
        Assert.assertEquals(geometry.getLocation().getLng(), listConverted.get(1).getLongitude());
        Assert.assertFalse(listConverted.get(1).getIsOpen());
        Assert.assertTrue(photoPlaces.get(0).getPhotoReference(), listConverted.get(1).getPhoto().contains("testphoto"));
        Assert.assertEquals(0, listConverted.get(1).getNumberOfPeopleEating());
        Assert.assertEquals(0, listConverted.get(1).getNumberOfFavorites());
    }

    @Test
    public void convertSingleRestaurantApiToRestaurantItemAllNullSuccess() {
        RestaurantApi restaurantApiBaseNull = new RestaurantApi();

        restaurantApiBaseNull.setName(null);
        restaurantApiBaseNull.setPlaceId(null);
        restaurantApiBaseNull.setVicinity(null);
        restaurantApiBaseNull.setGeometry(null);
        restaurantApiBaseNull.setOpeningHours(null);
        restaurantApiBaseNull.setPhotos(null);

        RestaurantItem convertedRestaurant =
                RestaurantMapper.apiToItem(restaurantApiBaseNull, photoBaseUrl, eatingAt, likedRestaurants, userLatitude, userLongitude);

        Assert.assertNull(convertedRestaurant.getName());
        Assert.assertNull(convertedRestaurant.getPlaceId());
        Assert.assertNull(convertedRestaurant.getAddress());
        Assert.assertNull(convertedRestaurant.getLatitude());
        Assert.assertNull(convertedRestaurant.getLongitude());
        Assert.assertFalse(convertedRestaurant.getIsOpen());
        Assert.assertNull(convertedRestaurant.getPhoto());
        Assert.assertEquals(0, convertedRestaurant.getNumberOfPeopleEating());
        Assert.assertEquals(0, convertedRestaurant.getNumberOfFavorites());
    }

    @Test
    public void convertSingleRestaurantApiToRestaurantItemSuccess() {
        RestaurantItem convertedRestaurant =
                RestaurantMapper.apiToItem(restaurantApiBase1, photoBaseUrl, eatingAt, likedRestaurants, userLatitude, userLongitude);

        Assert.assertEquals("test1name", convertedRestaurant.getName());
        Assert.assertEquals("test1placeid", convertedRestaurant.getPlaceId());
        Assert.assertEquals("test1vicinity", convertedRestaurant.getAddress());
        Assert.assertEquals(geometry.getLocation().getLat(), convertedRestaurant.getLatitude());
        Assert.assertEquals(geometry.getLocation().getLng(), convertedRestaurant.getLongitude());
        Assert.assertFalse(convertedRestaurant.getIsOpen());
        Assert.assertTrue(photoPlaces.get(0).getPhotoReference(), convertedRestaurant.getPhoto().contains("testphoto"));
        Assert.assertEquals(0, convertedRestaurant.getNumberOfPeopleEating());
        Assert.assertEquals(0, convertedRestaurant.getNumberOfFavorites());
    }

    @Test
    public void convertRestaurantDetailsApiModelToRestaurantDetailsSuccess() {
        com.bmathias.go4lunch.data.network.model.placesDetails.Photo photo = new com.bmathias.go4lunch.data.network.model.placesDetails.Photo();
        photo.setPhotoReference(photoReference);
        photoPlacesDetails.add(photo);

        restaurantDetailsApiModel1.setName("detailsTest1");
        restaurantDetailsApiModel1.setPlaceId("12345");
        restaurantDetailsApiModel1.setFormattedAddress("detailsTestAddress1");
        restaurantDetailsApiModel1.setFormattedPhoneNumber("detailsTestPhoneNumber1");
        restaurantDetailsApiModel1.setInternationalPhoneNumber("detailsTestInterPhoneNumber1");
        restaurantDetailsApiModel1.setWebsite("detailsTestWebsite1");
        restaurantDetailsApiModel1.setPhotos(photoPlacesDetails);

        RestaurantDetails restaurantDetailsConverted =
                RestaurantMapper.apiToDetails(restaurantDetailsApiModel1, photoBaseUrl, false, "1234");

        Assert.assertEquals("12345", restaurantDetailsConverted.getPlaceId());
        Assert.assertEquals("detailsTest1", restaurantDetailsConverted.getName());
        Assert.assertEquals("detailsTestAddress1", restaurantDetailsConverted.getAddress());
        Assert.assertEquals("detailsTestPhoneNumber1", restaurantDetailsConverted.getPhoneNumber());
        Assert.assertEquals("detailsTestWebsite1", restaurantDetailsConverted.getWebsite());
        Assert.assertTrue(photoPlacesDetails.get(0).getPhotoReference(), restaurantDetailsConverted.getPhotoUrl().contains("testphoto"));
    }

    @Test
    public void convertRestaurantDetailsApiModelToRestaurantDetailsSuccessWithNull() {
        com.bmathias.go4lunch.data.network.model.placesDetails.Photo photo = new com.bmathias.go4lunch.data.network.model.placesDetails.Photo();
        photo.setPhotoReference(photoReference);
        photoPlacesDetails.add(photo);

        restaurantDetailsApiModel1.setName("detailsTest1");
        restaurantDetailsApiModel1.setPlaceId("12345");
        restaurantDetailsApiModel1.setFormattedAddress("detailsTestAddress1");
        restaurantDetailsApiModel1.setFormattedPhoneNumber(null);
        restaurantDetailsApiModel1.setInternationalPhoneNumber(null);
        restaurantDetailsApiModel1.setWebsite(null);
        restaurantDetailsApiModel1.setPhotos(photoPlacesDetails);

        RestaurantDetails restaurantDetailsConverted =
                RestaurantMapper.apiToDetails(restaurantDetailsApiModel1, photoBaseUrl, false, "1234");

        Assert.assertEquals("12345", restaurantDetailsConverted.getPlaceId());
        Assert.assertEquals("detailsTest1", restaurantDetailsConverted.getName());
        Assert.assertEquals("detailsTestAddress1", restaurantDetailsConverted.getAddress());
        Assert.assertNull(restaurantDetailsConverted.getPhoneNumber());
        Assert.assertNull(restaurantDetailsConverted.getWebsite());
        Assert.assertTrue(photoPlacesDetails.get(0).getPhotoReference(), restaurantDetailsConverted.getPhotoUrl().contains("testphoto"));
    }

    @Test
    public void convertRestaurantDetailsApiModelToRestaurantDetailsSuccessAllNull() {
        com.bmathias.go4lunch.data.network.model.placesDetails.Photo photo = new com.bmathias.go4lunch.data.network.model.placesDetails.Photo();
        photo.setPhotoReference(photoReference);
        photoPlacesDetails.add(photo);

        restaurantDetailsApiModel1.setName(null);
        restaurantDetailsApiModel1.setPlaceId(null);
        restaurantDetailsApiModel1.setFormattedAddress(null);
        restaurantDetailsApiModel1.setFormattedPhoneNumber(null);
        restaurantDetailsApiModel1.setInternationalPhoneNumber(null);
        restaurantDetailsApiModel1.setWebsite(null);
        restaurantDetailsApiModel1.setPhotos(null);

        RestaurantDetails restaurantDetailsConverted =
                RestaurantMapper.apiToDetails(restaurantDetailsApiModel1, photoBaseUrl, false, "1234");

        Assert.assertNull(restaurantDetailsConverted.getPlaceId());
        Assert.assertNull(restaurantDetailsConverted.getName());
        Assert.assertNull( restaurantDetailsConverted.getAddress());
        Assert.assertNull(restaurantDetailsConverted.getPhoneNumber());
        Assert.assertNull(restaurantDetailsConverted.getWebsite());
        Assert.assertNull(restaurantDetailsConverted.getPhotoUrl());
    }
}