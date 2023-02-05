
package com.bmathias.go4lunch_.data.network.model.places;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantsApiResult {
    @SerializedName("results")
    @Expose
    private List<RestaurantApi> mRestaurantApis = null;

    public List<RestaurantApi> getResults() {
        return mRestaurantApis;
    }

    public void setResults(List<RestaurantApi> restaurantApis) {
        this.mRestaurantApis = restaurantApis;
    }

}
