package com.sydney.filmfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class UploadCsvActivity extends AppCompatActivity {

    private static final int PICK_CSV_REQUEST = 101;
    private Uri csvUri;

    private Button btnPickCsv;
    private Button btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_csv);

        btnPickCsv = findViewById(R.id.btn_pick_csv);
        btnUpload = findViewById(R.id.btn_upload);

        btnPickCsv.setOnClickListener(v -> openCsvPicker());
        btnUpload.setOnClickListener(v -> uploadCsvToFirestore());
    }

    private void openCsvPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");
        startActivityForResult(intent, PICK_CSV_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CSV_REQUEST && resultCode == RESULT_OK) {
            csvUri = data.getData();
        }
    }

    private void uploadCsvToFirestore() {
        if (csvUri != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            try {
                InputStream inputStream = getContentResolver().openInputStream(csvUri);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                CSVReader csvReader = new CSVReader(inputStreamReader);

                String[] values;
                // Skip the header row
                csvReader.readNext();

                while ((values = csvReader.readNext()) != null) {
                    String title = getValueSafely(values, 0);
                    String year = getValueSafely(values, 1);
                    String summary = getValueSafely(values, 2);
                    String shortSummary = getValueSafely(values, 3);
                    String imdbID = getValueSafely(values, 4);
                    String runtime = getValueSafely(values, 5);
                    String youtubeTrailer = getValueSafely(values, 6);
                    String rating = getValueSafely(values, 7);
                    String moviePoster = getValueSafely(values, 8);
                    String director = getValueSafely(values, 9);
                    String writers = getValueSafely(values, 10);
                    String cast = getValueSafely(values, 11);

                    // Create a new movie object
                    Map<String, Object> movie = new HashMap<>();
                    movie.put("Title", title);
                    movie.put("Year", year);
                    movie.put("Summary", summary);
                    movie.put("ShortSummary", shortSummary);
                    movie.put("IMDB_ID", imdbID);
                    movie.put("Runtime", runtime);
                    movie.put("YouTubeTrailer", youtubeTrailer);
                    movie.put("Rating", rating);
                    movie.put("MoviePoster", moviePoster);
                    movie.put("Director", director);
                    movie.put("Writers", writers);
                    movie.put("Cast", cast);

                    // Add the movie object to Firestore
                    db.collection("movies").add(movie);
                }

                csvReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getValueSafely(String[] values, int index) {
        if (index < values.length) {
            return values[index];
        }
        return "";  // or return null;
    }

}
