// GalleryFragment.java
package com.example.travelmap.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelmap.R;
import com.example.travelmap.adapter.ReviewAdapter;
import com.example.travelmap.model.Review;
import com.example.travelmap.storage.ReviewStorage;

import java.util.List;

public class GalleryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_gallery, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_gallery);
        TextView emptyTextView = view.findViewById(R.id.empty_text_view);

        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));

        ReviewStorage storage = new ReviewStorage(requireContext());
        List<Review> reviewList = storage.getAllReviews();

        if (reviewList.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTextView.setVisibility(View.GONE);
            ReviewAdapter adapter = new ReviewAdapter(requireContext(), reviewList);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }
}
