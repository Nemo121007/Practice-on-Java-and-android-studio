package com.example.photogallery;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.Settings;

import Photo.PhotoCollection;
import Photo.PhotoAdapter;
import Photo.Photo;
import android.Manifest;

import java.io.File;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity {
    ImageButton cameraButton, searchButton;
    RecyclerView recyclerView;
    private static PhotoCollection photos;
    private static final File photoGalleryDir = new File(Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "PhotoGallery");

    CameraHelper cameraHelper;
    PermissionHelper permissionHelper;

    private static final int REQUEST_CODE = 1; // Код запроса для READ_EXTERNAL_STORAGE
    private static final int REQUEST_CODE_MANAGE_EXTERNAL_STORAGE = 2; // Код запроса для MANAGE_EXTERNAL_STORAGE
    private static final int CAMERA_REQUEST_IMAGE_CAPTURE = 3; // Измененный код запроса
    private static final int CAMERA_REQUEST_PERMISSION_CODE = 4; // Измененный код запроса
    private static final int REQUEST_CODE_PHOTO_VIEW = 5; // Код запроса для просмотра
    private static final int REQUEST_CODE_SEARCH_PHOTO_VIEW = 6; // Код запроса для просмотра поиска

    public static int getREQUEST_CODE() {
        return REQUEST_CODE;
    }

    public static int getREQUEST_CODE_MANAGE_EXTERNAL_STORAGE() {
        return REQUEST_CODE_MANAGE_EXTERNAL_STORAGE;
    }

    public static int getCAMERA_REQUEST_IMAGE_CAPTURE(){
        return CAMERA_REQUEST_IMAGE_CAPTURE;
    }

    public static int getCAMERA_REQUEST_PERMISSION_CODE(){
        return CAMERA_REQUEST_PERMISSION_CODE;
    }

    public static int getREQUEST_CODE_PHOTO_VIEW(){
        return REQUEST_CODE_PHOTO_VIEW;
    }

    public static int getREQUEST_CODE_SEARCH_PHOTO_VIEW(){
        return REQUEST_CODE_SEARCH_PHOTO_VIEW;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // region Инициализация объектов
        cameraButton = findViewById(R.id.BackButton);
        searchButton = findViewById(R.id.imageSearchButton);

        recyclerView = findViewById(R.id.recyclerView);
        //endregion

        // region Обработка нажатий на кнопки
        cameraButton.setOnClickListener(v -> {
            cameraHelper.handleCameraButtonClick();
        });

        searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivityForResult(intent, REQUEST_CODE_PHOTO_VIEW);
        });
        // endregion

        // region Получение разрешений на работу с памятью
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else if (!Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_CODE_MANAGE_EXTERNAL_STORAGE);

        }
        //endregion

        // region Установка менеджера компоновки для RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        photos = PhotoCollection.ReadPhotoCollection(this);
//        photos.clear();
//        photos.WritePhotoCollection(this);

        assert photos != null;
        PhotoAdapter adapter = new PhotoAdapter(photos.getPhotoCollection()); // photos - список ваших фотографий
        adapter.setOnItemClickListener(position -> {
            Photo photo = photos.getPhotoCollection().get(position);
            Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
            intent.putExtra("photoId", photo.getId());
            startActivityForResult(intent, REQUEST_CODE_PHOTO_VIEW);
        });

        recyclerView.setAdapter(adapter);

        cameraHelper = new CameraHelper(this);
        permissionHelper = new PermissionHelper(this);
        // endregion
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

    public static PhotoCollection getPhotoCollection() {
        return photos;
    }

    public static File getPhotoGalleryDir() {
        return photoGalleryDir;
    }

    public static void updatePhoto(String photoId, @Nullable String name, @Nullable String descryption, @Nullable HashSet<String> tags) {
        Photo photo = photos.getPhotoFromId(photoId);
        if (photo != null) {
            if (name != null) {
                photo.setName(name);
            }
            if (descryption != null) {
                photo.setDescription(descryption);
            }
            if (tags != null) {
                photo.setTags(tags);
            }
        }
    }

    public void updateRecyclerView() {
        // Получение адаптера RecyclerView
        PhotoAdapter adapter = (PhotoAdapter) recyclerView.getAdapter();

        // Обновление данных адаптера
        adapter.updateData(photos.getPhotoCollection());

        // Уведомление адаптера об изменениях
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        permissionHelper.handlePermissionResult(requestCode, permissions, grantResults);

        cameraHelper.handleCameraPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        permissionHelper.handleActivityResult(requestCode, resultCode, data);

        cameraHelper.handleActivityResult(requestCode, resultCode, data);

        updateRecyclerView();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        photos.WritePhotoCollection(this);
    }
}