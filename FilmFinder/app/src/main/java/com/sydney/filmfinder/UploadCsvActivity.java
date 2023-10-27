package com.sydney.filmfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
            // Read and parse CSV, then upload to Firestore
            // ... (use the code provided in the previous message here)
        } else {
            Toast.makeText(this, "Please pick a CSV file first", Toast.LENGTH_SHORT).show();
        }
    }
}
