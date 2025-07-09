package com.example.travelmap.adapter;

import com.bumptech.glide.Glide;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelmap.R;
import com.example.travelmap.ReviewActivity;
import com.example.travelmap.model.Review;
import com.example.travelmap.storage.ReviewStorage;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private final Context context;
    private final List<Review> reviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewList.get(position);

        holder.regionNameText.setText(review.getRegionName());
        holder.dateText.setText(review.getDate());
        holder.ratingBar.setRating(review.getRating());
        holder.commentText.setText(review.getComment());

        if (review.getPhotoUri() != null && !review.getPhotoUri().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(review.getPhotoUri()))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)  // 에러 시 기본 이미지
                    .into(holder.photoView);
        } else {
            holder.photoView.setImageResource(R.drawable.placeholder);
        }

        // 삭제 버튼 클릭
        holder.deleteButton.setOnClickListener(v -> {
            Review reviewToDelete = reviewList.get(position);
            ReviewStorage storage = new ReviewStorage(context);
            storage.deleteReview(reviewToDelete);

            reviewList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, reviewList.size());

            Toast.makeText(context, "리뷰가 삭제되었습니다", Toast.LENGTH_SHORT).show();
        });

        // 수정 버튼 클릭
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReviewActivity.class);
            intent.putExtra("is_edit", true);
            intent.putExtra("region_code", review.getRegionCode());
            intent.putExtra("region_name", review.getRegionName());
            intent.putExtra("date", review.getDate());
            intent.putExtra("rating", review.getRating());
            intent.putExtra("photoUri", review.getPhotoUri());
            intent.putExtra("comment", review.getComment());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView regionNameText;
        TextView dateText;
        RatingBar ratingBar;
        ImageView photoView;
        TextView commentText;
        Button editButton;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            regionNameText = itemView.findViewById(R.id.review_region_name);
            dateText = itemView.findViewById(R.id.review_date);
            ratingBar = itemView.findViewById(R.id.review_rating);
            photoView = itemView.findViewById(R.id.review_photo);
            commentText = itemView.findViewById(R.id.review_comment);
            editButton = itemView.findViewById(R.id.btn_edit_review);
            deleteButton = itemView.findViewById(R.id.btn_delete_review);
        }
    }
}
