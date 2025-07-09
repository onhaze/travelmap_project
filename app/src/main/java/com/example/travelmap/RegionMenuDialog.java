package com.example.travelmap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.travelmap.model.GeoRegion;

public class RegionMenuDialog extends Dialog {

    public interface MenuListener {
        void onRegisterReview();
        void onViewReviews();
        void onCancel();
    }

    private final GeoRegion region;
    private final MenuListener menuListener;

    public RegionMenuDialog(Context context, GeoRegion region, MenuListener listener) {
        super(context);
        this.region = region;
        this.menuListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_region_menu);

        TextView title = findViewById(R.id.dialog_region_title);
        title.setText(region.getName());

        Button registerButton = findViewById(R.id.btn_register_review);
        Button viewReviewsButton = findViewById(R.id.btn_view_reviews);
        Button cancelButton = findViewById(R.id.btn_cancel);

        registerButton.setOnClickListener(v -> {
            if (menuListener != null) {
                menuListener.onRegisterReview();
            }
            dismiss();
        });

        viewReviewsButton.setOnClickListener(v -> {
            if (menuListener != null) {
                menuListener.onViewReviews();
            }
            dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            if (menuListener != null) {
                menuListener.onCancel();
            }
            dismiss();
        });
    }
}
