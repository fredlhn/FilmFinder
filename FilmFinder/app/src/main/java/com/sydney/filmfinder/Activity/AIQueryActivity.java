package com.sydney.filmfinder.Activity;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import okhttp3.Response;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.OkHttpClient;



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
import com.sydney.filmfinder.Activity.ResultActivity;
import com.sydney.filmfinder.Class.Movie;
import com.sydney.filmfinder.Class.MovieRecommendationRequest;
import com.sydney.filmfinder.Class.MovieRecommendationResponse;
import com.sydney.filmfinder.Interface.OpenAIApi;
import com.sydney.filmfinder.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.Index;

public class AIQueryActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();


    private EditText userInput;
    private TextView MovieFilter;
    private static final String ALGOLIA_APP_ID = "FXHWCT3O4N";
    private static final String ALGOLIA_SEARCH_KEY = "68a5472b28853764b5842bced7c01242";
    private static final String ALGOLIA_INDEX_NAME = "FilmFinder";

    private Client mClient;
    private Index mIndex;
    Button searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_query);

        userInput = findViewById(R.id.userInput);
        MovieFilter = findViewById(R.id.movieFilter);

        Button btnRecommend = findViewById(R.id.btnRecommend);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setEnabled(false);

        mClient = new Client(ALGOLIA_APP_ID, ALGOLIA_SEARCH_KEY);
        mIndex = mClient.getIndex(ALGOLIA_INDEX_NAME);
        btnRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = userInput.getText().toString();
                refineSearchWithGPT(description, refinedTitles -> {
                    if (refinedTitles != null && !refinedTitles.isEmpty()) {
                        List<String> titles = new ArrayList<>(refinedTitles);
                        //String firstTitle = refinedTitles.get(0).trim().toLowerCase();
                        searchForMovies(titles);
                    } else {
                        Toast.makeText(AIQueryActivity.this, "No refined title received.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filter = MovieFilter.getText().toString().trim().toLowerCase();
                //searchForMovie(filter);
            }
        });
    }


    private void searchForMovies(List<String> movieTitles) {
        Set<String> uniqueMovieIds = new HashSet<>();
        List<Movie> allMovies = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(movieTitles.size());

        for (String title : movieTitles) {
            Query query = new Query(title);
            mIndex.searchAsync(query, (content, error) -> {
                if (error == null) {
                    JSONArray hits = content.optJSONArray("hits");

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

                                // Only add the movie if its id is unique (not already in the set)
                                if (title.equalsIgnoreCase(movieTitle)) {
                                    if (uniqueMovieIds.add(id)) {
                                        Movie movie = new Movie(id, movieTitle, rating, cast, director, imdbId, moviePoster, ratingString,
                                                runtime, shortSummary, summary, writers, year, youTubeTrailer);
                                        allMovies.add(movie);
                                    }
                                }

                            }
                        }
                    }
                } else {
                    Log.e("AIQueryActivity", "Error searching for title: " + title);
                }

                if (counter.decrementAndGet() == 0) {
                    // All searches are completed
                    if (allMovies.size() > 0) {
                        allMovies.sort((movie1, movie2) -> Double.compare(movie2.getRating(), movie1.getRating()));
                        Intent intent = new Intent(AIQueryActivity.this, ResultActivity.class);
                        intent.putExtra("movies", new ArrayList<>(allMovies));
                        startActivity(intent);
                    } else {
                        Toast.makeText(AIQueryActivity.this, "No movies found.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }



    private void refineSearchWithGPT(String query, Callback<List<String>> callback) {
        new Thread(() -> {
            try {
                JSONObject json = new JSONObject();
                json.put("query", query);
                Log.d("AIQueryActivity", "Sending request to backend with query: " + query);

                RequestBody requestBody = RequestBody.create(json.toString(), okhttp3.MediaType.parse("application/json; charset=utf-8"));

                // Adjust this URL to point to your deployed backend.
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:3000/refine-search")
                        .post(requestBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    JSONObject responseBody = new JSONObject(response.body().string());
                    JSONArray titlesArray = responseBody.getJSONArray("splitResult");  // Parsing the "splitResult" key
                    List<String> titles = new ArrayList<>();
                    for (int i = 0; i < titlesArray.length(); i++) {
                        titles.add(titlesArray.getString(i));
                    }
                    Log.d("AIQueryActivity", "Received refined titles: " + titles);
                    if (callback != null) {
                        runOnUiThread(() -> {
                            try {
                                callback.onResult(titles);
                            } catch (Exception e) {
                                Log.e("AIQueryActivity", "Error in UI thread callback: " + e.getMessage());
                            }
                        });
                    } else {
                        Log.e("AIQueryActivity", "Callback is null!");
                    }
                } catch (Exception e) {
                    Log.e("AIQueryActivity", "Error inside onResult callback: " + e.getMessage());
                }
            } catch (Exception e) {
                Log.e("AIQueryActivity", "Error refining search: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(AIQueryActivity.this, "Error refining search: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private interface Callback<T> {
        void onResult(T result);
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