package Photo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PhotoCollection {
    @Expose
    private Map<String, Photo> photos;

    public PhotoCollection() {
        photos = new LinkedHashMap<>();
    }

    public PhotoCollection(@NonNull Photo... photos) {
        this();
        addPhotos(photos);
    }

    public PhotoCollection(@NonNull List<Photo> photos) {
        this();
        addPhotos(photos);
    }

    public void addPhoto(@NonNull Photo photo)
    {
        if (!photos.containsKey(photo.getId())){
            photos.put(photo.getId(), photo);
        } else {
            String newId = Integer.valueOf(photo.getId().hashCode()).toString();
            while (photos.containsKey(newId)){
                newId = Integer.valueOf(newId.hashCode()).toString();
            }

            // Рефлексия типов!!!
            try {
                Field idField = Photo.class.getDeclaredField("Id");
                idField.setAccessible(true);
                idField.set(photo, newId);
                photos.put(newId, photo);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                // Обработка ошибок
            }
        }
    }

    public void addPhotos(@NonNull Photo ... photos)
    {
        for (Photo photo : photos){
            if (photo != null){
                addPhoto(photo);
            }
        }
    }

    public void addPhotos(@NonNull List<Photo> photos)
    {
        for (Photo photo : photos){
            if (photo != null){
                addPhoto(photo);
            }
        }
    }

    public void removePhoto(@NonNull Photo photo) {
        photos.remove(photo.getId());
    }

    public void removePhoto(String photoId) {
        photos.remove(photoId);
    }

    public List<Photo> getPhotoCollection() {
        return new ArrayList<>(photos.values());
    }

    public List<Photo> getPhotoFromName(String name) {
        List<Photo> result = new ArrayList<>();
        for (Photo photo : photos.values()) {
            if (photo.getName().equals(name)) {
                result.add(photo);
            }
        }
        return result;
    }

    public List<Photo> getPhotoFromDescryption(String descryption) {
        List<Photo> result = new ArrayList<>();
        for (Photo photo : photos.values()) {
            if (photo.getDescription().contains(descryption)) {
                result.add(photo);
            }
        }
        return result;
    }

    public List<Photo> getPhotoFromTag(String tag) {
        List<Photo> result = new ArrayList<>();
        for (Photo photo : photos.values()) {
            if (photo.getTags().contains(tag)) {
                result.add(photo);
            }
        }
        return result;
    }

    public void WritePhotoCollection(@NonNull Context context) {
        File internalStorageDir = context.getFilesDir(); // Используйте context
        File myFile = new File(internalStorageDir, "dataPhotoCollection.json");

        try (FileWriter writer = new FileWriter(myFile)) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(this);
            writer.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace(); // Обработка IOException
        }
    }

    @Nullable
    public static PhotoCollection ReadPhotoCollection(@NonNull Context context) {
        File internalStorageDir = context.getFilesDir();
        File myFile = new File(internalStorageDir, "dataPhotoCollection.json");

        try (FileReader reader = new FileReader(myFile)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, PhotoCollection.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Или обработайте ошибку другим способом
        }
    }
}
