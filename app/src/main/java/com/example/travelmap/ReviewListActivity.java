package com.example.travelmap;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelmap.adapter.ReviewAdapter;
import com.example.travelmap.model.Review;
import com.example.travelmap.storage.ReviewStorage;

import java.util.List;

public class ReviewListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        String regionCode = getIntent().getStringExtra("region_code");
        String regionName = getIntent().getStringExtra("region_name");

        if (regionCode == null) {
            Toast.makeText(this, "지역 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (regionName == null) {
            regionName = "알 수 없는 지역";
        }

        TextView titleTextView = findViewById(R.id.recycler_view_review_list);
        titleTextView.setText(regionName + " 리뷰 목록");

        RecyclerView recyclerView = findViewById(R.id.recycler_view_review_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        ReviewStorage storage = new ReviewStorage(this);
        List<Review> reviewList = storage.getReviewsByRegion(regionCode);

        if (reviewList.isEmpty()) {
            Toast.makeText(this, "등록된 리뷰가 없습니다.", Toast.LENGTH_SHORT).show();
        }

        ReviewAdapter adapter = new ReviewAdapter(this, reviewList);
        recyclerView.setAdapter(adapter);
    }
}
