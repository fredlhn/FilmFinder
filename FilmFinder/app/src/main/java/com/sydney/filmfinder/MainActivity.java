package com.sydney.filmfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchForMovie(query);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a search query.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchForMovie(String title) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("movies")
                .whereEqualTo("title", title)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Movie> movies = new ArrayList<>();
                        task.getResult().forEach(document -> {
                            String id = document.getId();
                            String movieTitle = document.getString("title");
                            double rating = document.getDouble("rating");
                            movies.add(new Movie(id, movieTitle, rating));
                        });

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
