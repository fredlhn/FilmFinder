package com.sydney.filmfinder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private FirebaseFirestore db;
    private final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)     // Set read timeout to 60 seconds
            .connectTimeout(60, TimeUnit.SECONDS)  // Set connect timeout to 60 seconds
            .build();

    private ListView moviesListView;
    private ArrayAdapter<Movie> moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        moviesListView = findViewById(R.id.moviesListView);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SearchActivity", "Search button clicked");
                String query = searchEditText.getText().toString().trim();
                Log.d("SearchActivity", "Search query is: " + query);

                if (!query.isEmpty()) {
                    searchMovies(query);
                } else {
                    Toast.makeText(SearchActivity.this, "Please enter a search query.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchMovies(String query) {
        refineSearchWithGPT(query, refinedTitles -> {
            Log.d("SearchActivity", "Querying Firestore with titles: " + refinedTitles);

            Query firestoreQuery = createFirestoreQuery(refinedTitles);

            firestoreQuery.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            List<Movie> movies = snapshot.toObjects(Movie.class);
                            Log.d("SearchActivity", "Received " + movies.size() + " movies from Firestore");
                            displayMovies(movies);
                        } else {
                            Log.e("SearchActivity", "Firestore error: " + task.getException().getMessage());
                            Toast.makeText(SearchActivity.this, "Error fetching movies: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }


    private void refineSearchWithGPT(String query, Callback<List<String>> callback) {
        new Thread(() -> {
            try {
                JSONObject json = new JSONObject();
                json.put("query", query);
                Log.d("SearchActivity", "Sending request to backend with query: " + query);

                RequestBody requestBody = RequestBody.create(json.toString(), okhttp3.MediaType.parse("application/json; charset=utf-8"));

                // Adjust this URL to point to your deployed backend.
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:3000/refine-search")
                        .post(requestBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    JSONObject responseBody = new JSONObject(response.body().string());
                    List<String> titles = Arrays.asList(responseBody.getString("titles").split(",")); // Assuming titles are comma-separated
                    Log.d("SearchActivity", "Received refined titles: " + titles);
                    if (callback != null) {
                        runOnUiThread(() -> {
                            try {
                                callback.onResult(titles);
                            } catch (Exception e) {
                                Log.e("SearchActivity", "Error in UI thread callback: " + e.getMessage());
                            }
                        });
                    } else {
                        Log.e("SearchActivity", "Callback is null!");
                    }
                }
                catch (Exception e) {
                    Log.e("SearchActivity", "Error inside onResult callback: " + e.getMessage());
                }
            } catch (Exception e) {
                Log.e("SearchActivity", "Error refining search: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(SearchActivity.this, "Error refining search: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void displayMovies(List<Movie> movies) {
        if (moviesAdapter == null) {
            moviesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movies);
            moviesListView.setAdapter(moviesAdapter);
        } else {
            moviesAdapter.clear();
            moviesAdapter.addAll(movies);
            moviesAdapter.notifyDataSetChanged();
        }
    }

    private interface Callback<T> {
        void onResult(T result);
    }

    public static Map<String, String> parseRefinedQuery(String jsonString) {
        Map<String, String> resultMap = new HashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            // Extracting collection name
            if (jsonObject.has("collection_name")) {
                resultMap.put("collection", jsonObject.getString("collection_name"));
            }

            // Extracting field
            if (jsonObject.has("field")) {
                resultMap.put("field", jsonObject.getString("field"));
            }

            // Extracting operator
            if (jsonObject.has("operator")) {
                resultMap.put("operator", jsonObject.getString("operator"));
            }

            // Extracting value
            if (jsonObject.has("value")) {
                resultMap.put("value", jsonObject.getString("value"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }
    public Query createFirestoreQuery(List<String> titles) {
        CollectionReference collectionRef = db.collection("movies"); // Assuming the collection name is "movies"

        // Use the "in" operator in Firestore to match the movie titles
        Query firestoreQuery = collectionRef.whereIn("title", titles);

        return firestoreQuery;
    }

}
