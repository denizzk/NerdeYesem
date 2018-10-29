package com.karakaya.deniz.nerdeyesem.retrofit;

import com.karakaya.deniz.nerdeyesem.model.Restaurant;
import com.karakaya.deniz.nerdeyesem.model.ReviewRepo;
import com.karakaya.deniz.nerdeyesem.model.Search;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RestInterface {
    @Headers("Content-Type: application/json")
    @GET("api/v2.1/search")
    @RestaurantGson
    Observable<Search> getSearch(@Header("user-key") String userKey, @Query("lat") Double latitude,
                                 @Query("lon") Double longitude, @Query("sort") String sortBy,
                                 @Query("count") int count);

    @Headers("Content-Type: application/json")
    @GET("api/v2.1/restaurant")
    @GenericGson
    Observable<Restaurant> getRestaurant(@Header("user-key") String userKey, @Query("res_id") int
            restaurantId);

    @Headers("Content-Type: application/json")
    @GET("api/v2.1/reviews")
    @ReviewGson
    Observable<ReviewRepo> getReviewRepo(@Header("user-key") String userKey, @Query("res_id") int
            restaurantId);
}

@Retention(RetentionPolicy.RUNTIME)
@interface RestaurantGson {
}

@Retention(RetentionPolicy.RUNTIME)
@interface GenericGson {
}

@Retention(RetentionPolicy.RUNTIME)
@interface ReviewGson {
}