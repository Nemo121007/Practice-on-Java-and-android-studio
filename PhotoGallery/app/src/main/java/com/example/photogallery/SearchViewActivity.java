package com.example.photogallery;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.HashSet;

import Photo.Photo;
import Photo.PhotoAdapter;
import Photo.PhotoCollection;

public class SearchViewActivity extends AppCompatActivity {
    ImageButton BackButton;
    RecyclerView recyclerView;
    private static PhotoCollection filerPhotoCollection, photoCollectionFromMain;;

//    private final Activity activity; // Ссылка на MainActivity
//
//    public SearchViewActivity(Activity activity) {
//        this.activity = activity;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        // region Инициализация объектов
        BackButton = findViewById(R.id.BackButton);
        recyclerView = findViewById(R.id.recyclerView);
        //endregion

        filerPhotoCollection = SearchActivity.getPhotoCollectionFilter();

        // region Обработка нажатий на кнопки
        BackButton.setOnClickListener(v -> {
            finish();
        });
        // endregion

        // region Установка менеджера компоновки для RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        assert filerPhotoCollection != null;
        PhotoAdapter adapter = new PhotoAdapter(filerPhotoCollection.getPhotoCollection()); // photos - список ваших фотографий
        adapter.setOnItemClickListener(position -> {
            Photo photo = filerPhotoCollection.getPhotoCollection().get(position);
            Intent intent = new Intent(this, PhotoActivity.class);
            intent.putExtra("REQUEST_CODE", MainActivity.getREQUEST_CODE_SEARCH_PHOTO_VIEW());
            intent.putExtra("photoId", photo.getId());
            startActivityForResult(intent, MainActivity.getREQUEST_CODE_SEARCH_PHOTO_VIEW());
        });

        recyclerView.setAdapter(adapter);
    }

    public void updateRecyclerView() {
        // Получение адаптера RecyclerView
        PhotoAdapter adapter = (PhotoAdapter) recyclerView.getAdapter();

        // Обновление данных адаптера
        adapter.updateData(filerPhotoCollection.getPhotoCollection());

        // Уведомление адаптера об изменениях
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateRecyclerView();
    }
}