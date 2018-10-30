package com.karakaya.deniz.nerdeyesem.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

public class ReviewsActivity extends AppCompatActivity {

    ReviewRecyclerViewAdapter adapter = new ReviewRecyclerViewAdapter();
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    RestInterface restInterface;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private int restaurantId;
    private String restaurantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            restaurantName = getIntent().getStringExtra("restaurant-name");
        }
        if (!restaurantName.equals("")) {
            setTitle(restaurantName + " Reviews");
        }

        recyclerView = findViewById(R.id.reviews_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        restInterface = ApiClient.getClient().create(RestInterface.class);

        if (getIntent() != null) {
            restaurantId = Integer.parseInt(getIntent().getStringExtra("restaurant-id"));
        }
        if (restaurantId != 0) {
            getRestaurantReviews(restaurantId);
        }
    }

    private void getRestaurantReviews(int restaurantId) {
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

                    for (int i = 0; i < reviewRepo.getUserReviews().size(); i++) {
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
                })
        );
    }

}
