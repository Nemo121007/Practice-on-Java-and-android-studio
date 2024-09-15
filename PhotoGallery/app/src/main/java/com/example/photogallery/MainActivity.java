package com.example.photogallery;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity {
    ImageButton cameraButton, searchButton;
    RecyclerView recyclerView;
    private static PhotoCollection photos;
    private static final File photoGalleryDir = new File(Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "PhotoGallery");

    private static final int REQUEST_CODE = 1; // Код запроса для READ_EXTERNAL_STORAGE
    private static final int REQUEST_CODE_MANAGE_EXTERNAL_STORAGE = 2; // Код запроса для MANAGE_EXTERNAL_STORAGE
    private static final int CAMERA_REQUEST_IMAGE_CAPTURE = 2; // Измененный код запроса
    private static final int CAMERA_REQUEST_PERMISSION_CODE = 200; // Измененный код запроса

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // region Инициализация объектов
        cameraButton = findViewById(R.id.imageCameraButton);
        searchButton = findViewById(R.id.imageSearchButton);

        recyclerView = findViewById(R.id.recyclerView);
        //endregion

        // region Обработка нажатий на кнопки
        cameraButton.setOnClickListener(v -> {
            if (cameraCheckCameraPermission()) {
                cameraDispatchTakePictureIntent();
            } else {
                cameraRequestCameraPermission();
            }
        });

        searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
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
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
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

    public void writePhotoCollection() {
        photos.WritePhotoCollection(this);
    }

    // region Работа с разрешениями на чтение и запись
    private void checkManageExternalStoragePermission() {
        if (!Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_CODE_MANAGE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Разрешения на память
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkManageExternalStoragePermission();
            } else {
                finish();
            }
        }

        // Разрешения на камеру
        if (requestCode == CAMERA_REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraDispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Разрешение на камеру отклонено", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MANAGE_EXTERNAL_STORAGE) {
            if (!Environment.isExternalStorageManager()) {
                finish();
            }
        }

        // Работа с камерой
        if (requestCode == CAMERA_REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            if (imageBitmap != null) {
                saveImageToExternalStorage(imageBitmap);
            }
        }
    }
    // endregion

    // region Работа с камерой
    private boolean cameraCheckCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void cameraRequestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_PERMISSION_CODE);
    }

    private void cameraDispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_IMAGE_CAPTURE);
        }
    }
    // endregion

    // region Сохранение фотографии
    private void saveImageToExternalStorage(Bitmap imageBitmap) {
        if (isExternalStorageWritable()) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Обработка ошибки создания файла
                Toast.makeText(this, "Ошибка создания файла", Toast.LENGTH_SHORT).show();
            }
            // TODO: Проверить
            if (photoFile != null) {
                try (FileOutputStream out = new FileOutputStream(photoFile)) {
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    // Обновление списка фотографий
                    photos.addPhoto(new Photo(photoFile.getName()));
                    photos.WritePhotoCollection(this);
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (IOException e) {
                    // Обработка ошибки сохранения файла
                    Toast.makeText(this, "Ошибка сохранения файла", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // Внешнее хранилище недоступно для записи
            Toast.makeText(this, "Внешнее хранилище недоступно", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private File createImageFile() throws IOException {
        // Создание уникального имени файла
        long currentTime = System.currentTimeMillis();
        String timeStamp = new Date(currentTime).toString();
        String imageFileName = timeStamp.hashCode() + ".png"; // Изменено расширение на .png

        while (photos.getPhotoFromId(imageFileName) != null){
            imageFileName = imageFileName.hashCode() + ".png";
        }

        File image = new File(photoGalleryDir, imageFileName); // Создание файла с именем imageFileName
        return image;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    // endregion

    @Override
    protected void onDestroy(){
        super.onDestroy();
        photos.WritePhotoCollection(this);
    }
}