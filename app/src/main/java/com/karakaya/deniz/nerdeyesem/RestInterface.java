package com.karakaya.deniz.nerdeyesem;

import com.karakaya.deniz.nerdeyesem.model.Search;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RestInterface {
    @Headers("Content-Type: application/json")
    @GET("api/v2.1/search")
    Call<Search> getSearch(@Header("user-key") String userKey, @Query("lat") Double Latitude, @Query("lon") Double Longitude, @Query("sort") String sortBy, @Query("count") int count);
}
