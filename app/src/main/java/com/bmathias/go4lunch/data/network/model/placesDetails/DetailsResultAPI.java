
package com.bmathias.go4lunch.data.network.model.placesDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsResultAPI {

    @SerializedName("result")
    @Expose
    private RestaurantDetailsApiModel mRestaurantDetailsApiModel;

    public RestaurantDetailsApiModel getResult() {
        return mRestaurantDetailsApiModel;
    }

    public void setResult(RestaurantDetailsApiModel restaurantDetailsApiModel) {
        this.mRestaurantDetailsApiModel = restaurantDetailsApiModel;
    }
}
