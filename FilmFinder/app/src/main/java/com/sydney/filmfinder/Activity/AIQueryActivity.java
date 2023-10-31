package com.sydney.filmfinder.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class AIQueryActivity extends AppCompatActivity {

    private EditText userInput;
    private TextView MovieFilter;
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
                fetchMovieRecommendation(description);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filter = MovieFilter.getText().toString().trim().toLowerCase();
                searchForMovie(filter);
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
        MovieRecommendationRequest request = new MovieRecommendationRequest("gpt-3.5-turbo-instruct", "In Algolia's filter syntax, spaces within attribute values can cause issues. You need to wrap the string you're filtering on in double quotes if it includes spaces.like this title:\\\"war room\\\" Base on the information in brackets, get Agolia filter.（"+description+")");

        Call<MovieRecommendationResponse> call = service.getMovieRecommendation("application/json",API_KEY,request);
        call.enqueue(new Callback<MovieRecommendationResponse>() {
            @Override
            public void onResponse(Call<MovieRecommendationResponse> call, Response<MovieRecommendationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieFilter.setText(response.body().getCompletionText());
                    searchButton.setEnabled(true);
                } else {
                    MovieFilter.setText("Failed to get filter.");
                    searchButton.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<MovieRecommendationResponse> call, Throwable t) {
                MovieFilter.setText("Error: " + t.getMessage());
            }
        });
    }

    private void searchForMovie(String movieFilter) {
        Query query = new Query("war room").setFilters(movieFilter);
        mIndex.searchAsync(query, (content, error) -> {
            if (error == null) {
                JSONArray hits = content.optJSONArray("hits");
                ArrayList<Movie> movies = new ArrayList<>();

                if (hits != null) {
                    for (int i = 0; i < hits.length(); i++) {
                        JSONObject hit = hits.optJSONObject(i);
                        if (hit != null) {
                            String id = hit.optString("objectID");
                            String title = hit.optString("Title");
                            double rating = hit.optDouble("Rating");
                            Log.d("movie",id+"\n"+title);
                            movies.add(new Movie(id, title, rating));
                        }
                    }
                }

                if (movies.size() > 0) {
                    Intent intent = new Intent(AIQueryActivity.this, ResultActivity.class);
                    intent.putExtra("movies", movies);
                    startActivity(intent);
                } else {
                    Toast.makeText(AIQueryActivity.this, "No movies found.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AIQueryActivity.this, "Error searching. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

