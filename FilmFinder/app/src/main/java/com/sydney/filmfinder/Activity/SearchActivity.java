package com.sydney.filmfinder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.Index;

import com.sydney.filmfinder.Class.Movie;
import com.sydney.filmfinder.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private EditText searchEditText;

    // Algolia
    private static final String ALGOLIA_APP_ID = "FXHWCT3O4N";
    private static final String ALGOLIA_SEARCH_KEY = "68a5472b28853764b5842bced7c01242";
    private static final String ALGOLIA_INDEX_NAME = "FilmFinder";
    private Client mClient;
    private Index mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = findViewById(R.id.searchEditText);
        Button searchButton = findViewById(R.id.searchButton);

        mClient = new Client(ALGOLIA_APP_ID, ALGOLIA_SEARCH_KEY);
        mIndex = mClient.getIndex(ALGOLIA_INDEX_NAME);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString().trim().toLowerCase(); // Considering case insensitivity
                if (!query.isEmpty()) {
                    searchForMovie(query);
                } else {
                    Toast.makeText(SearchActivity.this, "Please enter a search query.", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    startActivity(new Intent(this, MainActivity.class));
//                    break;
//                case R.id.navigation_results:
//                    startActivity(new Intent(this, ResultActivity.class));
//                    break;
//                case R.id.navigation_details:
//                    startActivity(new Intent(this, MovieDetailActivity.class));
//                    break;
//            }
//            return true;
//        });
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
                            String title = hit.optString("Title");
                            double rating = hit.optDouble("Rating");
                            Log.d("movie",id+"\n"+title);
                            movies.add(new Movie(id, title, rating));
                        }
                    }
                }

                if (movies.size() > 0) {
                    Intent intent = new Intent(SearchActivity.this, ResultActivity.class);
                    intent.putExtra("movies", movies);
                    startActivity(intent);
                } else {
                    Toast.makeText(SearchActivity.this, "No movies found.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SearchActivity.this, "Error searching. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
