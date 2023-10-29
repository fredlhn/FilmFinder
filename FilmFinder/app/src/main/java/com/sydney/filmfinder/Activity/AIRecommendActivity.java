package com.sydney.filmfinder.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.sydney.filmfinder.R;

public class AIRecommendActivity extends AppCompatActivity {

    private EditText etKeywords;
    private Button btnRecommend;
    private TextView tvMovieRecommendation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_recommend);

        etKeywords = findViewById(R.id.etKeywords);
        btnRecommend = findViewById(R.id.btnRecommend);
        tvMovieRecommendation = findViewById(R.id.tvMovieRecommendation);

        btnRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keywords = etKeywords.getText().toString().trim();
                if (!keywords.isEmpty()) {
                    getMovieRecommendation(keywords);
                }
            }
        });
    }

    private void getMovieRecommendation(String keywords) {
        // Connect to ChatGPT or similar service here and get a movie recommendation
        // Update the tvMovieRecommendation with the movie title

        // This is a placeholder until you integrate with ChatGPT or similar service
        tvMovieRecommendation.setText("Recommended movie for " + keywords + ": [Movie Title]");
    }
}
