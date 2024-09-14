package com.example.photogallery;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.ChipGroup;

public class SearchActivity extends AppCompatActivity {
    ImageButton imageBackButton, imageOkButton;
    EditText editNameFieldText, editTextFieldDescryption;
    ChipGroup tagContainer;
    TextView textNameView, textTagView, textDescryptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        imageBackButton = findViewById(R.id.imageBackButton);
        imageOkButton = findViewById(R.id.imageOkButton);
        editNameFieldText = findViewById(R.id.editNameFieldText);
        editTextFieldDescryption = findViewById(R.id.editDescryptionFieldText);
        tagContainer = findViewById(R.id.tagContainer);
        textNameView = findViewById(R.id.textNameView);
        textTagView = findViewById(R.id.textTagView);
        textDescryptionView = findViewById(R.id.textDescryptionView);

        imageBackButton.setOnClickListener(v -> {
            finish();
        });

        imageOkButton.setOnClickListener(v -> {
            // TODO: Обработать сохранение
            finish();
        });

    }
}