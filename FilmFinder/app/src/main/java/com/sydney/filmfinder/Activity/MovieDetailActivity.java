package com.sydney.filmfinder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.sydney.filmfinder.Class.Movie;
import com.sydney.filmfinder.R;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView titleTextView, ratingTextView, castTextView, directorTextView, imdbIdTextView, runtimeTextView,
            shortSummaryTextView, summaryTextView, writersTextView, yearTextView;

    private ImageView moviePosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        titleTextView = findViewById(R.id.titleTextView);
        ratingTextView = findViewById(R.id.ratingTextView);
        castTextView = findViewById(R.id.castTextView);
        directorTextView = findViewById(R.id.directorTextView);
        imdbIdTextView = findViewById(R.id.imdbIdTextView);
        runtimeTextView = findViewById(R.id.runtimeTextView);
        shortSummaryTextView = findViewById(R.id.shortSummaryTextView);
        summaryTextView = findViewById(R.id.summaryTextView);
        writersTextView = findViewById(R.id.writersTextView);
        yearTextView = findViewById(R.id.yearTextView);

        moviePosterImageView = findViewById(R.id.moviePosterImageView);

        WebView youtubeWebView = findViewById(R.id.youtubeWebView);
        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        youtubeWebView.setWebChromeClient(new WebChromeClient());

        Movie selectedMovie = (Movie) getIntent().getSerializableExtra("selectedMovie");

        if (selectedMovie != null) {
            titleTextView.setText(selectedMovie.getTitle());
            ratingTextView.setText("Rating: " + selectedMovie.getRating() + "/10");
            castTextView.setText(selectedMovie.getCast());
            directorTextView.setText(selectedMovie.getDirector());
            imdbIdTextView.setText(selectedMovie.getIMDB_ID());
            runtimeTextView.setText(selectedMovie.getRuntime());
            shortSummaryTextView.setText(selectedMovie.getShortSummary());
            summaryTextView.setText(selectedMovie.getSummary());
            writersTextView.setText(selectedMovie.getWriters());
            yearTextView.setText(selectedMovie.getYear());

            Glide.with(this)
                    .load(selectedMovie.getMoviePoster())
                    .into(moviePosterImageView);
            String videoEmbedUrl = "https://www.youtube.com/embed/" + selectedMovie.getYouTubeTrailer();
            youtubeWebView.loadUrl(videoEmbedUrl);

        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
