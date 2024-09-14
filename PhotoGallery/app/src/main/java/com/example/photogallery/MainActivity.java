package com.example.photogallery;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.Settings;

import Photo.PhotoCollection;
import Photo.Photo;
import Photo.PhotoAdapter;
import android.Manifest;

public class MainActivity extends AppCompatActivity {
    ImageButton cameraButton, searchButton;
    RecyclerView recyclerView;

    private static final int REQUEST_CODE = 1; // Код запроса для READ_EXTERNAL_STORAGE
    private static final int REQUEST_CODE_MANAGE_EXTERNAL_STORAGE = 2; // Код запроса для MANAGE_EXTERNAL_STORAGE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else if (!Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_CODE_MANAGE_EXTERNAL_STORAGE);

        }

        cameraButton = findViewById(R.id.imageCameraButton);
        searchButton = findViewById(R.id.imageSearchButton);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        Photo photo = new Photo("test.png");
        PhotoCollection photoCollection = new PhotoCollection();
//        for (Integer i = 0; i < 30; i++) {
//            photoCollection.addPhoto(photo);
//        }

        photoCollection.WritePhotoCollection(this);
        photoCollection = PhotoCollection.ReadPhotoCollection(this);

        PhotoAdapter adapter = new PhotoAdapter(photoCollection.getPhotoCollection()); // photos - список ваших фотографий
        recyclerView.setAdapter(adapter);

    }

//    private void testPhoto(){
//        Photo photo = new Photo("test.png");
//        PhotoCollection photoCollection = new PhotoCollection();
//        for (Integer i = 0; i < 20; i++){
//            photoCollection.addPhoto(photo);
//        }
//        photoCollection.WritePhotoCollection(this);
//        PhotoCollection a = PhotoCollection.ReadPhotoCollection(this);
//    }

    private void loadPhotos() {
        // TODO: Добавьте логику для загрузки изображений из внешней памяти
        // и создания объектов Photo
    }

    private void checkManageExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_CODE_MANAGE_EXTERNAL_STORAGE);
            } else {
                // Разрешение MANAGE_EXTERNAL_STORAGE предоставлено
            }
        } else {
            // Для старых версий Android используем READ_EXTERNAL_STORAGE
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkManageExternalStoragePermission();
            } else {
                // Разрешение READ_EXTERNAL_STORAGE не предоставлено
                // TODO: Обработайте отказ, например, покажите сообщение пользователю
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MANAGE_EXTERNAL_STORAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // Разрешение MANAGE_EXTERNAL_STORAGE предоставлено
                } else {
                    // Разрешение MANAGE_EXTERNAL_STORAGE не предоставлено
                    // TODO: Обработайте отказ, например, покажите сообщение пользователю
                }
            }
        }
    }

}