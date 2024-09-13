package com.example.photogallery;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.ChipGroup;

public class SearchActivity extends AppCompatActivity {
    ImageButton cameraButton, searchButton;
    EditText editNameFieldText, editTextFieldDescryption;
    ChipGroup tagContainer;
    TextView textNameViev, textTagViev, textDescryptionViev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        cameraButton = findViewById(R.id.imageCameraButton);
        searchButton = findViewById(R.id.imageSearchButton);
        editNameFieldText = findViewById(R.id.editNameFieldText);
        editTextFieldDescryption = findViewById(R.id.editDescryptionFieldText);
        tagContainer = findViewById(R.id.tagContainer);
        textNameViev = findViewById(R.id.textNameView);
        textDescryptionViev = findViewById(R.id.textTagView);
        textDescryptionViev = findViewById(R.id.textDescryptionView);

    }
}