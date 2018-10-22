package com.karakaya.deniz.nerdeyesem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.List;

public class Search {
    @SerializedName("results_found")
    @Expose
    private String resultsFound;
    @SerializedName("restaurants")
    @Expose
    private List<Restaurant> restaurants;

    public String getResultsFound() {
        return resultsFound;
    }

    public void setResultsFound(String resultsFound) {
        this.resultsFound = resultsFound;
    }

    public List<Restaurant> getRestaurantList() {
        return restaurants;
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurants = restaurantList;
    }
}

