package com.example.photogallery;

import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import Photo.PhotoCollection;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.ChipGroup;

import java.io.File;

import Photo.Photo;

public class PhotoActivity extends AppCompatActivity {
    ImageButton backButton, deleteButton;
    ImageView image;
    EditText editNameText, editTextDescryption;
    ChipGroup tagContainer;
    Photo photo;
    PhotoCollection photoCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        // region Инициализация объектов
        backButton = findViewById(R.id.imageBackButton);
        deleteButton = findViewById(R.id.imageDeleteButton);
        image = findViewById(R.id.imagePhotoView);
        editNameText = findViewById(R.id.editNameText);
        editTextDescryption = findViewById(R.id.editDescryptionText);
        tagContainer = findViewById(R.id.tagContainer);
        // endregion

        // region Обработка нажатий кнопок
        backButton.setOnClickListener(v -> {
            finish();
        });

        deleteButton.setOnClickListener(v -> {
            // TODO: Обработать удаление
            finish();
        });
        // endregion

        photo = (Photo) getIntent().getSerializableExtra("photo");
        photoCollection = (PhotoCollection) getIntent().getSerializableExtra("photoCollection");
        // Получите путь к изображению из вашего объекта Photo
        assert photo != null;
        String imagePath = photo.getPath();

        // Создайте URI из пути к файлу
        File imageFile = new File(imagePath);
        Uri imageUri = Uri.fromFile(imageFile);

        // Загрузите изображение в ImageView с помощью Glide
        Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.question) // изображение-заполнитель
                .error(R.drawable.question) // изображение при ошибке
                .into(image);

        GestureDetector gestureDetector = new GestureDetector(this, new MyGestureListener());
        image.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    private void showPreviousData() {
        // Логика для получения предыдущего набора данных
        // и обновления полей: editNameText, editTextDescryption, tagContainer
        Photo previousPhoto = photoCollection.past(); //  ваш метод для получения предыдущего фото
        if (previousPhoto != null) {
            updateFields(previousPhoto);
        }
    }

    private void showNextData() {
        // Логика для получения следующего набора данных
        // и обновления полей: editNameText, editTextDescryption, tagContainer
        Photo nextPhoto = photoCollection.next(); //  ваш метод для получения следующего фото
        if (nextPhoto != null) {
            updateFields(nextPhoto);
        }
    }

    private void updateFields(@NonNull Photo photo) {
        editNameText.setText(photo.getName()); //  предполагается, что у Photo есть метод getName()
        editTextDescryption.setText(photo.getDescryption()); // предполагается, что у Photo есть метод getDescription()
        //  обновление tagContainer в зависимости от тегов фото
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                if (velocityX > 0) {
                    // Свайп вправо - показать предыдущий набор данных
                    showPreviousData();
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
        super.onDestroy();
         if (!photo.getName().equals(editNameText.getText().toString())){
             MainActivity.updatePhoto(photo.getId(), editNameText.getText().toString());
        }
        if (!photo.getDescryption().equals(editTextDescryption.getText().toString())){
            MainActivity.updatePhoto(photo.getId(), null, editTextDescryption.getText().toString());
        }
    }
}