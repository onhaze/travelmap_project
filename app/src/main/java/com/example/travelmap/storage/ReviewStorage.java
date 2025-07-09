// ReviewStorage.java
package com.example.travelmap.storage;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.travelmap.model.Review;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReviewStorage {
    private static final String PREFS_NAME = "reviews";
    private final SharedPreferences prefs;
    private final Gson gson = new Gson();

    public ReviewStorage(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void addReview(Review review) {
        List<Review> reviews = getAllReviews();
        reviews.add(review);
        saveAllReviews(reviews);
    }

    public void deleteReview(Review reviewToDelete) {
        List<Review> reviews = getAllReviews();
        Iterator<Review> iterator = reviews.iterator();
        while (iterator.hasNext()) {
            Review r = iterator.next();
            if (r.getRegionCode().equals(reviewToDelete.getRegionCode()) &&
                    r.getDate().equals(reviewToDelete.getDate())) {
                iterator.remove();
                break;
            }
        }
        saveAllReviews(reviews);
    }

    public List<Review> getAllReviews() {
        String json = prefs.getString("review_list", null);
        Type type = new TypeToken<List<Review>>() {}.getType();
        return json == null ? new ArrayList<>() : gson.fromJson(json, type);
    }

    public List<Review> getReviewsByRegion(String regionCode) {
        List<Review> all = getAllReviews();
        List<Review> filtered = new ArrayList<>();
        for (Review r : all) {
            if (r.getRegionCode().equals(regionCode)) {
                filtered.add(r);
            }
        }
        return filtered;
    }

    public boolean hasReview(String regionCode) {
        for (Review r : getAllReviews()) {
            if (r.getRegionCode().equals(regionCode)) return true;
        }
        return false;
    }

    private void saveAllReviews(List<Review> reviews) {
        String json = gson.toJson(reviews);
        prefs.edit().putString("review_list", json).apply();
    }

    public void addOrUpdateReview(Review review) {
        List<Review> reviews = getAllReviews();
        boolean updated = false;

        for (int i = 0; i < reviews.size(); i++) {
            Review r = reviews.get(i);
            if (r.getRegionCode().equals(review.getRegionCode()) &&
                    r.getDate().equals(review.getDate())) {
                // 수정 (update)
                reviews.set(i, review);
                updated = true;
                break;
            }
        }
        if (!updated) {
            reviews.add(review);
        }
        saveAllReviews(reviews);
    }
}
