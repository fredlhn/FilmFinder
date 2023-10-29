package com.sydney.filmfinder;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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
                    Toast.makeText(MainActivity.this, "Please enter a search query.", Toast.LENGTH_SHORT).show();
                }
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
                            String title = hit.optString("Title");
                            double rating = hit.optDouble("Rating");
                            Log.d("movie",id+"\n"+title);
                            movies.add(new Movie(id, title, rating));
                        }
                    }
                }

                if (movies.size() > 0) {
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("movies", movies);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "No movies found.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Error searching. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
