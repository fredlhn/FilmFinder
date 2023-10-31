package com.sydney.filmfinder.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.algolia.search.saas.Query;
import com.sydney.filmfinder.Class.Movie;
import com.sydney.filmfinder.Class.MovieRecommendationRequest;
import com.sydney.filmfinder.Class.MovieRecommendationResponse;
import com.sydney.filmfinder.Interface.OpenAIApi;
import com.sydney.filmfinder.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.Index;

public class AIRecommendActivity extends AppCompatActivity {

    private EditText userInput;
    private TextView MovieRecommendation;
    private static final String ALGOLIA_APP_ID = "FXHWCT3O4N";
    private static final String ALGOLIA_SEARCH_KEY = "68a5472b28853764b5842bced7c01242";
    private static final String ALGOLIA_INDEX_NAME = "FilmFinder";

    private Client mClient;
    private Index mIndex;
    Button searchButton;

    // Your base API endpoint
    private static final String BASE_URL = "https://api.openai.com/v1/";

    // Your API key
    private static final String API_KEY = "Bearer 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";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_recommend);

        userInput = findViewById(R.id.userInput);
        MovieRecommendation = findViewById(R.id.MovieRecommendation);

        Button btnRecommend = findViewById(R.id.btnRecommend);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setEnabled(false);

        mClient = new Client(ALGOLIA_APP_ID, ALGOLIA_SEARCH_KEY);
        mIndex = mClient.getIndex(ALGOLIA_INDEX_NAME);
        btnRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = userInput.getText().toString();
                fetchMovieRecommendation(description);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = MovieRecommendation.getText().toString().trim().toLowerCase();
                searchForMovie(query);
            }
        });
    }

    private void fetchMovieRecommendation(String description) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenAIApi service = retrofit.create(OpenAIApi.class);

        // Assuming a model and prompt setup for your use case
        MovieRecommendationRequest request = new MovieRecommendationRequest("gpt-3.5-turbo-instruct", "Base on the information in brackets, only generate one film name, the result does not need double quotation mark. （"+description+")");

        Call<MovieRecommendationResponse> call = service.getMovieRecommendation("application/json",API_KEY,request);
        call.enqueue(new Callback<MovieRecommendationResponse>() {
            @Override
            public void onResponse(Call<MovieRecommendationResponse> call, Response<MovieRecommendationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieRecommendation.setText(response.body().getCompletionText());
                    searchButton.setEnabled(true);
                } else {
                    MovieRecommendation.setText("Failed to get a recommendation.");
                    searchButton.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<MovieRecommendationResponse> call, Throwable t) {
                MovieRecommendation.setText("Error: " + t.getMessage());
            }
        });
    }

    private void searchForMovie(String queryText) {
        Query query = new Query(queryText);
        mIndex.searchAsync(query, (content, error) -> {
            if (error == null) {
                JSONArray hits = content.optJSONArray("hits");
                ArrayList<Movie> movies = new ArrayList<>();

                if (hits != null) {
                    for (int i = 0; i < hits.length(); i++) {
                        JSONObject hit = hits.optJSONObject(i);
                        if (hit != null) {
                            String id = hit.optString("objectID");
                            String movieTitle = hit.optString("Title");

                            double rating = hit.optDouble("Rating");
                            String cast = hit.optString("Cast");
                            String director = hit.optString("Director");
                            String imdbId = hit.optString("IMDB_ID");
                            String moviePoster = hit.optString("MoviePoster");
                            String ratingString = hit.optString("Rating");
                            String runtime = hit.optString("Runtime");
                            String shortSummary = hit.optString("ShortSummary");
                            String summary = hit.optString("Summary");
                            String writers = hit.optString("Writers");
                            String year = hit.optString("Year");
                            String youTubeTrailer = hit.optString("YouTubeTrailer");

                            Movie movie = new Movie(id, movieTitle, rating, cast, director, imdbId, moviePoster, ratingString,
                                    runtime, shortSummary, summary, writers, year, youTubeTrailer);
                            movies.add(movie);
                        }
                    }
                }

                if (movies.size() > 0) {
                    Intent intent = new Intent(AIRecommendActivity.this, ResultActivity.class);
                    intent.putExtra("movies", movies);
                    startActivity(intent);
                } else {
                    Toast.makeText(AIRecommendActivity.this, "No movies found.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AIRecommendActivity.this, "Error searching. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
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
