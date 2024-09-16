package com.example.photogallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import Photo.Photo;
import Photo.PhotoCollection;

class CameraHelper {
    private final Activity activity; // Ссылка на MainActivity

    private static final File photoGalleryDir = MainActivity.getPhotoGalleryDir();
    private PhotoCollection photos = MainActivity.getPhotoCollection();

    CameraHelper(Activity activity) {
        this.activity = activity;
        photos = MainActivity.getPhotoCollection();
    }

    private boolean cameraCheckCameraPermission() {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void cameraRequestCameraPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, MainActivity.getCAMERA_REQUEST_PERMISSION_CODE());
    }

    private void cameraDispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, MainActivity.getCAMERA_REQUEST_IMAGE_CAPTURE());
        }
    }

    void handleCameraButtonClick() {
        if (cameraCheckCameraPermission()) {
            cameraDispatchTakePictureIntent();
        } else {
            cameraRequestCameraPermission();
        }
    }

    void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.getCAMERA_REQUEST_IMAGE_CAPTURE() && resultCode == Activity.RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            if (imageBitmap != null) {
                saveImageToExternalStorage(imageBitmap);
            }
        }
    }

    void handleCameraPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MainActivity.getCAMERA_REQUEST_PERMISSION_CODE()) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraDispatchTakePictureIntent();
            } else {
                Toast.makeText(activity, "Разрешение на камеру отклонено", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // region Сохранение фотографии
    public void saveImageToExternalStorage(Bitmap imageBitmap) {
        if (isExternalStorageWritable()) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Обработка ошибки создания файла
                Toast.makeText(activity, "Ошибка создания файла", Toast.LENGTH_SHORT).show();
            }
            // TODO: Проверить
            if (photoFile != null) {
                try (FileOutputStream out = new FileOutputStream(photoFile)) {
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    // Обновление списка фотографий
                    Photo photo = new Photo(Photo.GeneratorId(photoFile.getAbsolutePath()));
                    photos.addPhoto(photo);
                    photos.WritePhotoCollection(activity);
                } catch (IOException e) {
                    // Обработка ошибки сохранения файла
                    Toast.makeText(activity, "Ошибка сохранения файла", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // Внешнее хранилище недоступно для записи
            Toast.makeText(activity, "Внешнее хранилище недоступно", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private File createImageFile() throws IOException {
        // Создание уникального имени файла
        String imageFileName = UUID.randomUUID().toString() + ".png";
        File imageFile = new File(photoGalleryDir, imageFileName);
        while (imageFile.exists()) {
            imageFileName = UUID.randomUUID().toString() + ".png";
            imageFile = new File(photoGalleryDir, imageFileName);
        }  imageFile = new File(photoGalleryDir, imageFileName);

        File image = new File(photoGalleryDir, imageFileName); // Создание файла с именем imageFileName
        return image;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    // endregion
}