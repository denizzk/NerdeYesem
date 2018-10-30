package com.karakaya.deniz.nerdeyesem.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.karakaya.deniz.nerdeyesem.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter
        .ReviewViewHolder> {

    private List<Integer> mReviewIds = new ArrayList<>();
    private List<String> mReviewTexts = new ArrayList<>();
    private List<Integer> mReviewRatings = new ArrayList<>();
    private List<String> mReviewRatingTexts = new ArrayList<>();
    private List<String> mReviewRatingColors = new ArrayList<>();
    private List<String> mReviewDates = new ArrayList<>();
    private List<Integer> mReviewLikes = new ArrayList<>();
    private List<Integer> mReviewCommentsCount = new ArrayList<>();
    private List<String> mUserNames = new ArrayList<>();
    private List<String> mUserProfileUrls = new ArrayList<>();
    private List<String> mUserLevels = new ArrayList<>();
    private List<String> mUserLevelColors = new ArrayList<>();
    private List<String> mUserImages = new ArrayList<>();
    private Context mContext;

    public ReviewRecyclerViewAdapter(List<Integer> mReviewIds, List<String> mReviewTexts,
                                     List<Integer> mReviewRatings, List<String>
                                             mReviewRatingTexts, List<String>
                                             mReviewRatingColors, List<String> mReviewTimes,
                                     List<Integer> mReviewLikes, List<Integer> mReviewCommentsCount,
                                     List<String> mUserNames, List<String> mUserProfileUrls,
                                     List<String> mUserLevels, List<String> mUserLevelColors,
                                     List<String> mUserImages, Context mContext) {
        this.mReviewIds = mReviewIds;
        this.mReviewTexts = mReviewTexts;
        this.mReviewRatings = mReviewRatings;
        this.mReviewRatingTexts = mReviewRatingTexts;
        this.mReviewRatingColors = mReviewRatingColors;
        this.mReviewDates = mReviewTimes;
        this.mReviewLikes = mReviewLikes;
        this.mReviewCommentsCount = mReviewCommentsCount;
        this.mUserNames = mUserNames;
        this.mUserProfileUrls = mUserProfileUrls;
        this.mUserLevels = mUserLevels;
        this.mUserLevelColors = mUserLevelColors;
        this.mUserImages = mUserImages;
        this.mContext = mContext;
    }

    public ReviewRecyclerViewAdapter() {

    }

    @NonNull
    @Override
    public ReviewRecyclerViewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup
                                                                                 parent, int
                                                                                 viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listuser,
                parent, false);
        ReviewRecyclerViewAdapter.ReviewViewHolder holder = new ReviewRecyclerViewAdapter
                .ReviewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRecyclerViewAdapter.ReviewViewHolder holder,
                                 final int
                                         position) {
        if (!mUserImages.get(position).equals("")) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(mUserImages.get(position))
                    .into(holder.userImage);
        } else {
            Glide.with(mContext)
                    .asBitmap()
                    .load(R.mipmap.ic_launcher_round)
                    .into(holder.userImage);
        }
        holder.userName.setText(mUserNames.get(position));
        holder.userName.setOnClickListener(v -> {
            Intent intent = new Intent("android.intent.action.VIEW",
                    Uri.parse(mUserProfileUrls.get(position)));
            mContext.startActivity(intent);
        });
        holder.userLevel.setText(mUserLevels.get(position));
        holder.userLevel.setTextColor(Color.parseColor("#" + mUserLevelColors
                .get(position)));
        holder.userReviewDate.setText(mReviewDates.get(position));
        holder.userReviewRating.setText("" + mReviewRatings.get(position).doubleValue());
        holder.userReviewRating.setBackgroundColor(Color.parseColor("#" + mReviewRatingColors
                .get(position)));
        holder.userComment.setText(mReviewTexts.get(position));
    }

    @Override
    public int getItemCount() {
        return mReviewIds.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName;
        TextView userLevel;
        TextView userReviewDate;
        TextView userReviewRating;
        TextView userComment;
        LinearLayout parentLayout;

        private ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
            userLevel = itemView.findViewById(R.id.user_level);
            userReviewDate = itemView.findViewById(R.id.user_review_date);
            userReviewRating = itemView.findViewById(R.id.user_review_rating);
            userComment = itemView.findViewById(R.id.user_comment);
            parentLayout = itemView.findViewById(R.id.user_parent_layout);
        }
    }
}
