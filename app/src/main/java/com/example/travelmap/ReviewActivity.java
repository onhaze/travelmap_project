package com.example.travelmap;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.travelmap.model.Review;
import com.example.travelmap.storage.ReviewStorage;
import java.io.IOException;
import java.util.Calendar;

public class ReviewActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private String regionCode;
    private String regionName;
    private TextView dateView;
    private RatingBar ratingBar;
    private ImageView imageView;
    private Uri imageUri;
    private EditText commentEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        regionCode = getIntent().getStringExtra("region_code");
        regionName = getIntent().getStringExtra("region_name");

        TextView title = findViewById(R.id.region_title);
        title.setText(regionName);

        dateView = findViewById(R.id.date_text);
        ratingBar = findViewById(R.id.review_rating);
        imageView = findViewById(R.id.photo_view);
        commentEdit = findViewById(R.id.edit_comment);

        Button dateButton = findViewById(R.id.date_button);
        dateButton.setOnClickListener(v -> showDatePicker());

        Button photoButton = findViewById(R.id.photo_button);
        photoButton.setOnClickListener(v -> chooseImage());

        boolean isEdit = getIntent().getBooleanExtra("is_edit", false);

        if (isEdit) {
            String date = getIntent().getStringExtra("date");
            float rating = getIntent().getFloatExtra("rating", 0f);
            String photoUriString = getIntent().getStringExtra("photoUri");
            String comment = getIntent().getStringExtra("comment");

            dateView.setText(date);
            ratingBar.setRating(rating);
            commentEdit.setText(comment);

            if (photoUriString != null) {
                imageUri = Uri.parse(photoUriString);
                imageView.setImageURI(imageUri);
            }
        }

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            String date = dateView.getText().toString();
            float rating = ratingBar.getRating();
            String comment = commentEdit.getText().toString();

            ReviewStorage storage = new ReviewStorage(this);

            Review review = new Review(regionCode, regionName, date, rating,
                    imageUri != null ? imageUri.toString() : null, comment);

            storage.addOrUpdateReview(review);

            Toast.makeText(this, "리뷰가 저장되었습니다", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String date = year + "/" + (month + 1) + "/" + dayOfMonth;
                    dateView.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
