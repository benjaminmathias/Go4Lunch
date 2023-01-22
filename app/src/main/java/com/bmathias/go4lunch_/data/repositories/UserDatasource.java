package com.bmathias.go4lunch_.data.repositories;

import java.util.List;

import io.reactivex.Observable;

public interface UserDatasource {

    Observable<List<String>> getNonDistinctSelectedRestaurantIds();

    Observable<List<String>> getNonDistinctFavoritedRestaurantIds();

    Observable<Boolean> getCurrentUserFavoriteObservable(String placeId);

    void updateSelectedRestaurant(String placeId, String placeName);

    void deleteSelectedRestaurant();

    void addFavoriteRestaurant(String placeId);

    void removeFavoriteRestaurant(String placeId);
}
