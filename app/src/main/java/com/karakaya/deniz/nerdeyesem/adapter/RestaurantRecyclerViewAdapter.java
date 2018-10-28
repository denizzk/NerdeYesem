package com.karakaya.deniz.nerdeyesem.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.karakaya.deniz.nerdeyesem.R;
import com.karakaya.deniz.nerdeyesem.activity.RestaurantDetailActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RestaurantRecyclerViewAdapter extends RecyclerView
        .Adapter<RestaurantRecyclerViewAdapter.ViewHolder> {

    private List<String> mRestaurantIds = new ArrayList<>();
    private List<String> mRestaurantImages = new ArrayList<>();
    private List<String> mRestaurantNames = new ArrayList<>();
    private List<String> mRestaurantLocations = new ArrayList<>();
    private List<String> mRestaurantCuisines = new ArrayList<>();
    private List<String> mRestaurantRatings = new ArrayList<>();
    private List<String> mRestaurantRatingColors = new ArrayList<>();
    private List<String> mRestaurantPrices = new ArrayList<>();
    private List<String> mRestaurantCurrencies = new ArrayList<>();
    private Location mlocation;
    private Context mContext;

    public RestaurantRecyclerViewAdapter(List<String> mRestaurantIds, List<String> mImages,
                                         List<String>
            mRestaurantNames, List<String>
                                                 mRestaurantLocations, List<String>
                                                 mRestaurantCuisines, List<String>
                                                 mRestaurantRatings, List<String>
                                                 mRestaurantRatingColors,
                                         List<String> mRestaurantPrices, List<String>
                                                 mRestaurantCurrencies, Location location,
                                         Context mContext) {
        this.mRestaurantIds = mRestaurantIds;
        this.mRestaurantImages = mImages;
        this.mRestaurantNames = mRestaurantNames;
        this.mRestaurantLocations = mRestaurantLocations;
        this.mRestaurantCuisines = mRestaurantCuisines;
        this.mRestaurantRatings = mRestaurantRatings;
        this.mRestaurantRatingColors = mRestaurantRatingColors;
        this.mRestaurantPrices = mRestaurantPrices;
        this.mRestaurantCurrencies = mRestaurantCurrencies;
        this.mlocation = location;
        this.mContext = mContext;
    }

    public RestaurantRecyclerViewAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,
                parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (!mRestaurantImages.get(position).equals("")) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(mRestaurantImages.get(position))
                    .into(holder.restaurantImage);
        } else {
            Glide.with(mContext)
                    .asBitmap()
                    .load(R.mipmap.res_holder)
                    .into(holder.restaurantImage);
        }
        holder.restaurantName.setText(mRestaurantNames.get(position));
        holder.restaurantLocation.setText(mRestaurantLocations.get(position));
        holder.restaurantCuisine.setText(mRestaurantCuisines.get(position));
        holder.restaurantRating.setText("" + Double.parseDouble(mRestaurantRatings.get(position)));
        holder.restaurantRating.setBackgroundColor(Color.parseColor("#" + mRestaurantRatingColors
                .get(position)));
        holder.restaurantAvgPrice.setText("Avg. price: " + mRestaurantCurrencies.get(position) +
                Double
                        .parseDouble(mRestaurantPrices
                                .get(position)) / 2);

        holder.parentLayout.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
            intent.putExtra("restaurant_id", mRestaurantIds.get(position));
            intent.putExtra("location", mlocation);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mRestaurantIds.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView restaurantImage;
        TextView restaurantName;
        TextView restaurantLocation;
        TextView restaurantCuisine;
        TextView restaurantRating;
        TextView restaurantAvgPrice;
        RelativeLayout parentLayout;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantImage = itemView.findViewById(R.id.restaurant_image);
            restaurantName = itemView.findViewById(R.id.restaurant_name);
            restaurantLocation = itemView.findViewById(R.id.restaurant_location);
            restaurantCuisine = itemView.findViewById(R.id.restaurant_cuisine);
            restaurantRating = itemView.findViewById(R.id.restaurant_rating);
            restaurantAvgPrice = itemView.findViewById(R.id.restaurant_price);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
