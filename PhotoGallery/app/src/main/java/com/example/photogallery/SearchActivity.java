package com.example.photogallery;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.HashSet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import Photo.PhotoCollection;

public class SearchActivity extends AppCompatActivity {
    ImageButton imageBackButton, imageOkButton, addButton;
    EditText editNameFieldText, editTextFieldDescryption;
    ChipGroup tagContainer;
    TextView textNameView, textTagView, textDescryptionView;

    String name, descryption;
    HashSet<String> tags;
    PhotoCollection photoCollectionFromMain;
    static PhotoCollection photoCollectionFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        imageBackButton = findViewById(R.id.imageBackButton);
        imageOkButton = findViewById(R.id.imageOkButton);
        addButton = findViewById(R.id.addButton);
        editNameFieldText = findViewById(R.id.editNameFieldText);
        editTextFieldDescryption = findViewById(R.id.editDescryptionFieldText);
        tagContainer = findViewById(R.id.tagContainer);
        textNameView = findViewById(R.id.textNameView);
        textTagView = findViewById(R.id.textTagView);
        textDescryptionView = findViewById(R.id.textDescryptionView);

        imageBackButton.setOnClickListener(v -> {
            setResult(MainActivity.getREQUEST_CODE_PHOTO_VIEW());
            finish();
        });

        imageOkButton.setOnClickListener(v -> {
            search();
        });

        addButton.setOnClickListener(v -> {
            showAddTag();
        });

        photoCollectionFromMain = MainActivity.getPhotoCollection();
    }

    private void search() {
        name = editNameFieldText.getText().toString();
        descryption = editTextFieldDescryption.getText().toString();
        tags = new HashSet<>();
        for (int i = 0; i < tagContainer.getChildCount(); i++) {
            View child = tagContainer.getChildAt(i);
            if (child instanceof Chip) {
                Chip chip = (Chip) child;
                tags.add(chip.getText().toString());
            }
        }

        photoCollectionFilter = photoCollectionFromMain.getFilterPhotoCollection(name, descryption, tags);

        Intent intent = new Intent(this, SearchViewActivity.class);
        intent.putExtra("REQUEST_CODE", MainActivity.getREQUEST_CODE_SEARCH_PHOTO_VIEW());
        startActivityForResult(intent, MainActivity.getREQUEST_CODE_SEARCH_PHOTO_VIEW());
    }

    public static PhotoCollection getPhotoCollectionFilter() {
        return photoCollectionFilter;
    }

    private void showAddTag(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите тег");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { // Устанавливаем кнопки до create()
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredText = input.getText().toString();
                addTag(enteredText);
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                input.requestFocus();
            }
        });

        dialog.show();
    }

    private void addTag(String tag){
        Chip chip = (Chip) this.getLayoutInflater().inflate(R.layout.chip_template, tagContainer, false);
        chip.setText(tag);
        chip.setOnCloseIconClickListener(v -> {
            tagContainer.removeView(chip);
        });
        tagContainer.addView(chip);
    }
}