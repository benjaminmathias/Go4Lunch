
package com.bmathias.go4lunch_.data.network.model.placesDetails;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsResultAPI {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("result")
    @Expose
    private RestaurantDetailsApiModel mRestaurantDetailsApiModel;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public RestaurantDetailsApiModel getResult() {
        return mRestaurantDetailsApiModel;
    }

    public void setResult(RestaurantDetailsApiModel restaurantDetailsApiModel) {
        this.mRestaurantDetailsApiModel = restaurantDetailsApiModel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
