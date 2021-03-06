
package com.bmathias.go4lunch.network.model.RestaurantPlaceDetailsResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsResultAPI {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("result")
    @Expose
    private RestaurantDetailsAPI mRestaurantDetailsAPI;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public RestaurantDetailsAPI getResult() {
        return mRestaurantDetailsAPI;
    }

    public void setResult(RestaurantDetailsAPI restaurantDetailsAPI) {
        this.mRestaurantDetailsAPI = restaurantDetailsAPI;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
