package com.sydney.filmfinder;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView ratingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        titleTextView = findViewById(R.id.titleTextView);
        ratingTextView = findViewById(R.id.ratingTextView);

        Movie selectedMovie = (Movie) getIntent().getSerializableExtra("selectedMovie");

        if (selectedMovie != null) {
            titleTextView.setText(selectedMovie.getTitle());
            ratingTextView.setText("Rating: " + selectedMovie.getRating() + "/10");
        }
    }
}
