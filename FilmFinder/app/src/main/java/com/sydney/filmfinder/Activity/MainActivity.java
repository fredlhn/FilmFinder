package com.sydney.filmfinder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.sydney.filmfinder.R;

public class MainActivity extends AppCompatActivity {

    private Button btnSearchActivity;
    private Button btnAIRecommendActivity;

    private Button btnAIQueryActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearchActivity = findViewById(R.id.btnSearchActivity);
        btnAIRecommendActivity = findViewById(R.id.btnAIRecommendActivity);
        btnAIQueryActivity = findViewById(R.id.btnAIQueryActivity);


        btnSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        btnAIRecommendActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aiRecommendIntent = new Intent(MainActivity.this, AIRecommendActivity.class);
                startActivity(aiRecommendIntent);
            }
        });

        btnAIQueryActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aiQueryIntent = new Intent(MainActivity.this, AIQueryActivity.class);
                startActivity(aiQueryIntent);
            }
        });
    }
}
