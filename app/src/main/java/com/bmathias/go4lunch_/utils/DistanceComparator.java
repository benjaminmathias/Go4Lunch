package com.bmathias.go4lunch_.utils;

import com.bmathias.go4lunch_.data.model.RestaurantItem;

import java.util.Comparator;

/**
 * Class used to sort our list of restaurant by distance in ListFragment
 */
public class DistanceComparator implements Comparator<RestaurantItem> {
   @Override
   public int compare(RestaurantItem r1, RestaurantItem r2) {
      return Float.compare(r1.getDistance(), r2.getDistance());
   }
}
