package com.example.rockpaperscissors;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import GameSettings.GameSettings;

public class SettingsActivity extends AppCompatActivity {

    ImageButton canselButton, okButton;
    EditText editTextNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.settings, new SettingsFragment())
//                    .commit();
//        }
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);

        canselButton = findViewById(R.id.canselSettingsButton);
        okButton = findViewById(R.id.okSettingsButton);

        editTextNumber = findViewById(R.id.editTextNumber);
        editTextNumber.setText(String.valueOf(GameSettings.getMaxScore()));

        this.canselButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SettingsActivity.this, com.example.rockpaperscissors.MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        this.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameSettings.setMaxScore(Integer.parseInt(editTextNumber.getText().toString()));
//                Intent intent = new Intent(SettingsActivity.this, com.example.rockpaperscissors.MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }
}