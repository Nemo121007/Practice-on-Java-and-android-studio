package com.example.photogallery;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Photo.PhotoCollection;
import Photo.Photo;
import Photo.PhotoAdapter;

public class MainActivity extends AppCompatActivity {
    ImageButton cameraButton, searchButton;
    RecyclerView recyclerView;

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

        cameraButton = findViewById(R.id.imageCameraButton);
        searchButton = findViewById(R.id.imageSearchButton);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        Photo photo = new Photo("test.png");
        PhotoCollection photoCollection = new PhotoCollection();
        for (Integer i = 0; i < 20; i++){
            photoCollection.addPhoto(photo);
        }
        photoCollection.WritePhotoCollection(this);
        PhotoCollection a = PhotoCollection.ReadPhotoCollection(this);

        PhotoAdapter adapter = new PhotoAdapter(photoCollection.getPhotoCollection()); // photos - список ваших фотографий
        recyclerView.setAdapter(adapter);
    }

    private void testPhoto(){
        Photo photo = new Photo("test.png");
        PhotoCollection photoCollection = new PhotoCollection();
        for (Integer i = 0; i < 20; i++){
            photoCollection.addPhoto(photo);
        }
        photoCollection.WritePhotoCollection(this);
        PhotoCollection a = PhotoCollection.ReadPhotoCollection(this);
    }
}