// PermissionHelper.java (новый класс)
package com.example.photogallery;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

class PermissionHelper {
    private final Activity activity; // Ссылка на MainActivity

    PermissionHelper(Activity activity) {
        this.activity = activity;
    }

    private void checkManageExternalStoragePermission() {
        if (!Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            activity.startActivityForResult(intent, MainActivity.getREQUEST_CODE_MANAGE_EXTERNAL_STORAGE());
        }
    }

    void handleStoragePermissionRequest() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MainActivity.getREQUEST_CODE());
        } else {
            checkManageExternalStoragePermission();
        }
    }

    void handlePermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MainActivity.getREQUEST_CODE()) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkManageExternalStoragePermission();
            } else {
                activity.finish();
            }
        }
    }

    void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.getREQUEST_CODE_MANAGE_EXTERNAL_STORAGE()) {
            if (!Environment.isExternalStorageManager()) {
                activity.finish();
            }
        }
    }
}