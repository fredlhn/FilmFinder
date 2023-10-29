package com.sydney.filmfinder.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.sydney.filmfinder.Class.MovieRecommendationRequest;
import com.sydney.filmfinder.Class.MovieRecommendationResponse;
import com.sydney.filmfinder.Interface.OpenAIApi;
import com.sydney.filmfinder.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AIRecommendActivity extends AppCompatActivity {

    private EditText userInput;
    private TextView MovieRecommendation;

    // Your base API endpoint
    private static final String BASE_URL = "https://api.openai.com/";

    // Your API key
    private static final String API_KEY = "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";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_recommend);  // Assuming your XML file is named activity_airecommendation.xml

        userInput = findViewById(R.id.userInput);
        MovieRecommendation = findViewById(R.id.MovieRecommendation);

        Button btnRecommend = findViewById(R.id.btnRecommend);
        btnRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = userInput.getText().toString();
                fetchMovieRecommendation(description);
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
        MovieRecommendationRequest request = new MovieRecommendationRequest("gpt-3.5-turbo-instruct", description);

        Call<MovieRecommendationResponse> call = service.getMovieRecommendation(API_KEY, request);
        call.enqueue(new Callback<MovieRecommendationResponse>() {
            @Override
            public void onResponse(Call<MovieRecommendationResponse> call, Response<MovieRecommendationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieRecommendation.setText(response.body().getRecommendedMovieTitle());
                } else {
                    MovieRecommendation.setText("Failed to get a recommendation.");
                }
            }

            @Override
            public void onFailure(Call<MovieRecommendationResponse> call, Throwable t) {
                MovieRecommendation.setText("Error: " + t.getMessage());
            }
        });
    }
}
