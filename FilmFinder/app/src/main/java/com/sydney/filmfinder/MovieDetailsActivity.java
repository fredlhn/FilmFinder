package com.sydney.filmfinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sydney.filmfinder.Movie;
import com.sydney.filmfinder.R;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView movieDetailsTextView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movieDetailsTextView = findViewById(R.id.movieDetailsTextView);

        db = FirebaseFirestore.getInstance();

        String movieId = getIntent().getStringExtra("movieId");

        fetchMovieDetails(movieId);
    }

    private void fetchMovieDetails(String movieId) {
        db.collection("movies").document(movieId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Movie movie = documentSnapshot.toObject(Movie.class);
                    // Assuming Movie class has a custom method to get details as String
                    movieDetailsTextView.setText(movie.getDetailsAsString());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MovieDetailsActivity.this, "Error fetching movie details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}
