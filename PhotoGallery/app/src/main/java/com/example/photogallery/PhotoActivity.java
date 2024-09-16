package com.example.photogallery;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import Photo.PhotoCollection;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import Photo.Photo;

public class PhotoActivity extends AppCompatActivity {
    ImageButton backButton, deleteButton, imageButtonNext, imageButtonPast, plusButton;
    ImageView image;
    EditText editNameText, editTextDescryption;
    ChipGroup tagContainer;
    Photo photo;
    PhotoCollection photoCollection = MainActivity.getPhotoCollection();
    LayoutInflater inflater;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        inflater = LayoutInflater.from(this);

        // region Инициализация объектов
        backButton = findViewById(R.id.imageBackButton);
        deleteButton = findViewById(R.id.imageDeleteButton);
        imageButtonNext = findViewById(R.id.imageButtonNext);
        imageButtonPast = findViewById(R.id.imageButtonPast);
        plusButton = findViewById(R.id.plusButton);
        image = findViewById(R.id.imagePhotoView);
        editNameText = findViewById(R.id.editNameText);
        editTextDescryption = findViewById(R.id.editDescryptionText);
        tagContainer = findViewById(R.id.tagContainer);
        // endregion

        // region Обработка нажатий кнопок
        backButton.setOnClickListener(v -> {
            savePhotoCollection();
            finish();
        });

        deleteButton.setOnClickListener(v -> {
            photoCollection.removePhoto(photo);
            photoCollection.WritePhotoCollection(this);
            setResult(MainActivity.getREQUEST_CODE_PHOTO_VIEW());
            finish();
        });

        imageButtonNext.setOnClickListener(v -> {
            showNextData();
        });

        imageButtonPast.setOnClickListener(v -> {
            showPastData();
        });

        plusButton.setOnClickListener(v -> {
            showAddTag();
        });
        // endregion

        photo = photoCollection.getPhotoFromId((String) getIntent().getSerializableExtra("photoId"));

        // region Загрузка изображений
        // Создайте URI из пути к файлу

//        File imageFile = new File(MainActivity.getPhotoGalleryDir(), photo.getId());
//        Uri imageUri = Uri.fromFile(imageFile);
//
//        // Загрузите изображение в ImageView с помощью Glide
//        Glide.with(this)
//                .load(imageUri)
//                .placeholder(R.drawable.question) // изображение-заполнитель
//                .error(R.drawable.question) // изображение при ошибке
//                .into(image);
        updateFields();

        GestureDetector gestureDetector = new GestureDetector(this, new MyGestureListener());
        image.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
        // endregion

    }

    //
    private void addTag(String tag) {
        photo.getTags().add(tag);
        photoCollection.getPhotoFromId(photo.getId()).addTag(tag);
        Chip chip = (Chip) inflater.inflate(R.layout.chip_template, tagContainer, false);
        chip.setText(tag);
        chip.setOnCloseIconClickListener(v -> {
            photoCollection.getPhotoFromId(photo.getId()).removeTag(tag);
            tagContainer.removeView(chip);
            savePhotoCollection();
        });
        tagContainer.addView(chip);
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

    private void showPastData() {
        // Логика для получения предыдущего набора данных
        // и обновления полей: editNameText, editTextDescryption, tagContainer
        savePhotoCollection();
        photo = photoCollection.past(); //  ваш метод для получения предыдущего фото
        updateFields();
    }

    private void showNextData() {
        // Логика для получения следующего набора данных
        // и обновления полей: editNameText, editTextDescryption, tagContainer
        savePhotoCollection();
        photo = photoCollection.next(photo.getId()); //  ваш метод для получения следующего фото
        updateFields();
    }

    private void updateFields() {
        // Создайте URI из пути к файлу
        File imageFile = new File(MainActivity.getPhotoGalleryDir(), photo.getId());
        Uri imageUri = Uri.fromFile(imageFile);

        // Загрузите изображение в ImageView с помощью Glide
        Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.question) // изображение-заполнитель
                .error(R.drawable.question) // изображение при ошибке
                .into(image);

        editNameText.setText(photo.getName()); //  предполагается, что у Photo есть метод getName()
        editTextDescryption.setText(photo.getDescryption()); // предполагается, что у Photo есть метод getDescription()
        tagContainer.removeAllViews();
        for (String tag : photo.getTags()) {
            addTag(tag);
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                if (velocityX > 0) {
                    // Свайп вправо - показать предыдущий набор данных
                    showPastData();
                } else {
                    // Свайп влево - показать следующий набор данных
                    showNextData();
                }
                return true;
            }
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        savePhotoCollection();
        super.onDestroy();
    }

    private void savePhotoCollection() {
        if (!photo.getName().equals(editNameText.getText().toString())){
            MainActivity.updatePhoto(photo.getId(), editNameText.getText().toString(), null, null);
        }
        if (!photo.getDescryption().equals(editTextDescryption.getText().toString())){
            MainActivity.updatePhoto(photo.getId(), null, editTextDescryption.getText().toString(), null);
        }

        HashSet<String> tags = new HashSet<>();
        for (int i = 0; i < tagContainer.getChildCount(); i++) {
            View child = tagContainer.getChildAt(i);
            if (child instanceof Chip) {
                Chip chip = (Chip) child;
                tags.add(chip.getText().toString());
            }
        }
        if (!photo.getTags().equals(tags)){
            MainActivity.updatePhoto(photo.getId(), null, null, tags);
        }

        photoCollection.WritePhotoCollection(this);
    }
}