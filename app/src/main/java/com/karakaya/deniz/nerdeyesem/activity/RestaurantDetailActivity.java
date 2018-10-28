package com.karakaya.deniz.nerdeyesem.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karakaya.deniz.nerdeyesem.Common;
import com.karakaya.deniz.nerdeyesem.R;
import com.karakaya.deniz.nerdeyesem.adapter.ReviewRecyclerViewAdapter;
import com.karakaya.deniz.nerdeyesem.retrofit.ApiClient;
import com.karakaya.deniz.nerdeyesem.retrofit.RestInterface;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RestaurantDetailActivity extends AppCompatActivity {

    RestInterface restInterface;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    ReviewRecyclerViewAdapter adapter = new ReviewRecyclerViewAdapter();
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    TextView restaurantName, restaurantLocation, restaurantRating,
            restaurantIsOpenNow, restaurantVoteCount, restaurantCuisine, restaurantAvgCost,
            restaurantAddress, restaurantReviewCount;
    LinearLayout restaurantWebsiteButton, restaurantShowOnMap, restaurantReviews;
    ImageView restaurantImage, restaurantHasOnlineDelivery, restaurantHasTableReservation;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBarLayout;

    int restaurantId;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        recyclerView = findViewById(R.id.user_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        restInterface = ApiClient.getClient().create(RestInterface.class);

        appBarLayout = findViewById(R.id.app_bar_layout);

        restaurantImage = findViewById(R.id.img_restaurant);
        restaurantName = findViewById(R.id.restaurant_name);
        restaurantLocation = findViewById(R.id.restaurant_location);
        restaurantRating = findViewById(R.id.restaurant_rating);
        restaurantIsOpenNow = findViewById(R.id.restaurant_is_open_now);
        restaurantVoteCount = findViewById(R.id.restaurant_vote_count);
        restaurantWebsiteButton = findViewById(R.id.restaurant_website_button);
        restaurantCuisine = findViewById(R.id.restaurant_cuisine);
        restaurantHasOnlineDelivery = findViewById(R.id.restaurant_has_online_delivery);
        restaurantHasTableReservation = findViewById(R.id.restaurant_has_table_reservation);
        restaurantAvgCost = findViewById(R.id.restaurant_avg_cost);
        restaurantAddress = findViewById(R.id.restaurant_address);
        restaurantShowOnMap = findViewById(R.id.restaurant_on_map);
        restaurantReviews = findViewById(R.id.restaurant_all_reviews);
        restaurantReviewCount = findViewById(R.id.restaurant_review_count);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
//        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);


        if (getIntent() != null) {
            restaurantId = Integer.parseInt(getIntent().getStringExtra("restaurant_id"));
        }
        if (restaurantId != 0) {
            getRestaurantDetails(restaurantId);
        }

    }

    private void getRestaurantDetails(int restaurantId) {
        compositeDisposable.add(restInterface.getRestaurant("3fb383c9094715d5f7d994a43dbbefca",
                restaurantId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurant -> {
                    appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
                        if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                            // Collapsed
                            collapsingToolbarLayout.setTitle(restaurant.getName());
                        } else {
                            // Expanded
                            collapsingToolbarLayout.setTitle("");
                        }
                    });

                    if (restaurant.getFeaturedImage() != null) {
                        Glide.with(getBaseContext())
                                .load(restaurant.getFeaturedImage())
                                .into(restaurantImage);
                    } else {
                        Glide.with(getBaseContext())
                                .asBitmap()
                                .load(R.mipmap.res_holder)
                                .into(restaurantImage);
                    }
                    restaurantName.setText(restaurant.getName());
                    restaurantLocation.setText(restaurant.getLocation().getLocality() + " " +
                            restaurant.getLocation().getCity());
                    restaurantRating.setText("" + Double.parseDouble(restaurant.getUserRating()
                            .getAggregateRating()));
                    restaurantRating.setBackgroundColor(Color.parseColor("#" + restaurant
                            .getUserRating().getRatingColor()));

                    if (restaurant.getIsDeliveringNow().equals("1")) {
                        restaurantIsOpenNow.setText("Open Now");
                        restaurantIsOpenNow.setTextColor(Color.parseColor("#3FBF3F"));
                    } else {
                        restaurantIsOpenNow.setText("Closed Now");
                        restaurantIsOpenNow.setTextColor(Color.parseColor("#BF3F3F"));
                    }

                    restaurantVoteCount.setText(restaurant.getUserRating().getVotes() + " Votes");
                    restaurantWebsiteButton.setOnClickListener(v -> {
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse(restaurant.getUrl()));
                        startActivity(viewIntent);
                    });

                    restaurantCuisine.setText(restaurant.getCuisines());

                    if (restaurant.getHasOnlineDelivery().equals("1")) {
                        Glide.with(getBaseContext())
                                .load(R.drawable.ic_check_circle_green_30dp)
                                .into(restaurantHasOnlineDelivery);
                    } else {
                        Glide.with(getBaseContext())
                                .load(R.drawable.ic_remove_circle_red_30dp)
                                .into(restaurantHasOnlineDelivery);
                    }

                    if (restaurant.getHasTableBooking().equals("1")) {
                        Glide.with(getBaseContext())
                                .load(R.drawable.ic_check_circle_green_30dp)
                                .into(restaurantHasTableReservation);
                    } else {
                        Glide.with(getBaseContext())
                                .load(R.drawable.ic_remove_circle_red_30dp)
                                .into(restaurantHasTableReservation);
                    }

                    restaurantAvgCost.setText(restaurant.getCurrency() + " " + Double.parseDouble
                            (restaurant.getAverageCostForTwo()) / 2);

                    restaurantAddress.setText(restaurant.getLocation().getAddress());

                    restaurantShowOnMap.setOnClickListener(v -> {

                        Common common = new Common();
                        Location location = common.getLocation(getBaseContext());

                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse("http://www.google.com/maps/dir/" +
                                                location.getLatitude() + ","
                                                + location.getLongitude() +
                                                "/" + restaurant.getLocation().getLatitude() + "," +
                                                "" + restaurant.getLocation().getLongitude()));
                        startActivity(viewIntent);
                    });
                })
        );

        getRestaurantReviews();

    }

    private void getRestaurantReviews() {
        compositeDisposable.add(restInterface.getReviewRepo("3fb383c9094715d5f7d994a43dbbefca",
                restaurantId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reviewRepo -> {
                    List<Integer> reviewIds = new ArrayList<>();
                    List<String> reviewTexts = new ArrayList<>();
                    List<Integer> reviewRatings = new ArrayList<>();
                    List<String> reviewRatingTexts = new ArrayList<>();
                    List<String> reviewRatingColors = new ArrayList<>();
                    List<String> reviewDates = new ArrayList<>();
                    List<Integer> reviewLikes = new ArrayList<>();
                    List<Integer> reviewCommentsCount = new ArrayList<>();
                    List<String> userNames = new ArrayList<>();
                    List<String> userProfileUrls = new ArrayList<>();
                    List<String> userLevels = new ArrayList<>();
                    List<String> userLevelColors = new ArrayList<>();
                    List<String> userImages = new ArrayList<>();

                    int reviewsShown;
                    if (reviewRepo.getReviewsCount() < 2)
                        reviewsShown = reviewRepo.getReviewsCount();
                    else reviewsShown = 2;

                    for (int i = 0; i < reviewsShown; i++) {
                        reviewIds.add(i, reviewRepo.getUserReviews().get(i)
                                .getId());
                        reviewTexts.add(i, reviewRepo.getUserReviews().get(i)
                                .getReviewText());
                        reviewRatings.add(i, reviewRepo.getUserReviews().get(i)
                                .getRating());
                        reviewRatingTexts.add(i, reviewRepo.getUserReviews().get(i)
                                .getRatingText());
                        reviewRatingColors.add(i, reviewRepo.getUserReviews().get(i)
                                .getRatingColor());
                        reviewDates.add(i, reviewRepo.getUserReviews().get(i)
                                .getReviewTimeFriendly());
                        reviewLikes.add(i, reviewRepo.getUserReviews().get(i).getLikes());
                        reviewCommentsCount.add(i, reviewRepo.getUserReviews().get(i)
                                .getCommentsCount());
                        userNames.add(i, reviewRepo.getUserReviews()
                                .get(i)
                                .getUser().getName());
                        userProfileUrls.add(i, reviewRepo.getUserReviews()
                                .get(i)
                                .getUser().getProfileUrl());
                        userLevels.add(i, reviewRepo.getUserReviews()
                                .get(i)
                                .getUser().getFoodieLevel());
                        userLevelColors.add(i, reviewRepo.getUserReviews()
                                .get(i)
                                .getUser().getFoodieColor());
                        userImages.add(i, reviewRepo.getUserReviews()
                                .get(i)
                                .getUser().getProfileImage());

                    }
                    if (reviewRepo.getReviewsCount() > 0) {
                        adapter = new ReviewRecyclerViewAdapter(reviewIds, reviewTexts,
                                reviewRatings, reviewRatingTexts, reviewRatingColors,
                                reviewDates,
                                reviewLikes, reviewCommentsCount, userNames,
                                userProfileUrls,
                                userLevels, userLevelColors, userImages, this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.addItemDecoration(new
                                HorizontalDividerItemDecoration.Builder
                                (getApplicationContext()).build());
                    }

                    restaurantReviewCount.setText("All Reviews (" + reviewRepo.getReviewsCount()
                            + ") >");

                    restaurantReviews.setOnClickListener(v -> {
                        if (reviewsShown == 0) {
                            Toast.makeText(getBaseContext(), "No Reviews Found!", Toast
                                    .LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(getBaseContext(), ReviewsActivity.class);
                            intent.putExtra("restaurant-id", Integer.toString(restaurantId));
                            startActivity(intent);
                        }
                    });


                })
        );
    }
}
