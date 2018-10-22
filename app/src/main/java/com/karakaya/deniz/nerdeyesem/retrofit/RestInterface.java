package com.karakaya.deniz.nerdeyesem.retrofit;

import com.karakaya.deniz.nerdeyesem.model.Search;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RestInterface {
    @Headers("Content-Type: application/json")
    @GET("api/v2.1/search")
    Observable<Search> getSearch(@Header("user-key") String userKey, @Query("lat") Double latitude, @Query("lon") Double longitude, @Query("sort") String sortBy, @Query("count") int count);
}
