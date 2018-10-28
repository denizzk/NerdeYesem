package com.karakaya.deniz.nerdeyesem.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.karakaya.deniz.nerdeyesem.Common;
import com.karakaya.deniz.nerdeyesem.R;
import com.karakaya.deniz.nerdeyesem.adapter.RestaurantRecyclerViewAdapter;
import com.karakaya.deniz.nerdeyesem.model.Search;
import com.karakaya.deniz.nerdeyesem.retrofit.ApiClient;
import com.karakaya.deniz.nerdeyesem.retrofit.RestInterface;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;



public class MainActivity extends AppCompatActivity {

    RestInterface restInterface;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    RestaurantRecyclerViewAdapter adapter = new RestaurantRecyclerViewAdapter();
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

//    private LocationManager locationManager;
//    private LocationListener locationListener;

    private SwipeRefreshLayout swipeRefreshLayout;

    Common common=new Common();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.restaurant_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        restInterface = ApiClient.getClient().create(RestInterface.class);

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh, R.color.refresh1, R.color
                .refresh2);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            (new Handler()).postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);

                fetchData();
            }, 3000);
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .ACCESS_FINE_LOCATION}, 1);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        // Make sure that internet connection is enabled on the device
//        if (isOnline()) {
//            // after that make sure that GPS is enabled on the device
//            isGpsEnabled();
//        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                moveTaskToBack(true);
                finish();
            }
//        }
    }

    private void fetchData() {
        if (common.getLocation(this) != null) {
            Location location = common.getLocation(this);
            compositeDisposable.add(restInterface.getSearch
                    ("3fb383c9094715d5f7d994a43dbbefca",
                            location.getLatitude(), location.getLongitude(),
                            "real_distance", 5)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(search -> {
                        List<String> restaurantIds = new ArrayList<>();
                        List<String> restaurantNames = new ArrayList<>();
                        List<String> restaurantImages = new ArrayList<>();
                        List<String> restaurantLocations = new ArrayList<>();
                        List<String> restaurantCuisines = new ArrayList<>();
                        List<String> restaurantRatings = new ArrayList<>();
                        List<String> restaurantRatingColors = new ArrayList<>();
                        List<String> restaurantPrices = new ArrayList<>();
                        List<String> restaurantCurrencies = new ArrayList<>();

                        for (int i = 0; i < search.getRestaurantList().size(); i++) {
                            restaurantIds.add(i, search.getRestaurantList().get(i)
                                    .getId());
                            restaurantNames.add(i, search.getRestaurantList().get(i)
                                    .getName());
                            restaurantImages.add(i, search.getRestaurantList().get(i)
                                    .getThumb());
                            restaurantLocations.add(i, search.getRestaurantList().get(i)
                                    .getLocation().getAddress());
                            restaurantCuisines.add(i, search.getRestaurantList().get(i)
                                    .getCuisines());
                            restaurantRatings.add(i, search.getRestaurantList().get(i)
                                    .getUserRating().getAggregateRating());
                            restaurantRatingColors.add(i, search.getRestaurantList()
                                    .get(i)
                                    .getUserRating().getRatingColor());
                            restaurantPrices.add(i, search.getRestaurantList().get(i)
                                    .getAverageCostForTwo());
                            restaurantCurrencies.add(i, search.getRestaurantList()
                                    .get(i)
                                    .getCurrency());
                        }

                        adapter = new RestaurantRecyclerViewAdapter(restaurantIds,
                                restaurantImages,
                                restaurantNames, restaurantLocations,
                                restaurantCuisines,
                                restaurantRatings, restaurantRatingColors,
                                restaurantPrices, restaurantCurrencies, location,
                                MainActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.addItemDecoration(new
                                HorizontalDividerItemDecoration.Builder
                                (getApplicationContext()).build());
                    })
            );
        }
    }

}
