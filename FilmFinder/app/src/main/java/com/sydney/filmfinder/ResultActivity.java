package com.sydney.filmfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private ListView moviesListView;
    private ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        moviesListView = findViewById(R.id.moviesListView);

        movies = (ArrayList<Movie>) getIntent().getSerializableExtra("movies");

//        ArrayAdapter<Movie> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movies);

        MovieAdapter adapter = new MovieAdapter(this, movies);

        moviesListView.setAdapter(adapter);

        moviesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie selectedMovie = movies.get(position);
                Intent intent = new Intent(ResultActivity.this, MovieDetailActivity.class);
                intent.putExtra("selectedMovie", selectedMovie);
                startActivity(intent);
            }
        });
    }
}
