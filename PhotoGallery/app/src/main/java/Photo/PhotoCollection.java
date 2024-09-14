package Photo;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import kotlin.jvm.Transient;

public class PhotoCollection implements Serializable {
    @Expose
    private LinkedHashMap<String, Photo> photos;

    @Transient // Исключаем из сериализации
    private List<String> listPhotoId;
    @Transient
    private Integer index;

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
        }
//        else {
//            String newId = Integer.valueOf(photo.getId().hashCode()).toString();
//            while (photos.containsKey(newId)){
//                newId = Integer.valueOf(newId.hashCode()).toString();
//            }
//
//            // Рефлексия типов!!!
//            try {
//                Field idField = Photo.class.getDeclaredField("Id");
//                idField.setAccessible(true);
//                idField.set(photo, newId);
//                photos.put(newId, photo);
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                e.printStackTrace();
//                // Обработка ошибок
//            }
//        }
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

    public void clear(){
        photos.clear();
    }

    public List<Photo> getPhotoCollection() {
        return new ArrayList<>(photos.values());
    }

    public Photo getPhotoFromId(String id) {
        if (!photos.containsKey(id)){
            return null;
        }
        return photos.get(id);
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
            if (photo.getDescryption().contains(descryption)) {
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

    public Photo next() {
        if (listPhotoId.equals(null)){
            listPhotoId = new ArrayList<>(photos.keySet());
            index = 0;
        }
        else {
            if (index == listPhotoId.size()){
                index = 0;
            }
            else {
                index++;
            }
            return photos.get(listPhotoId.get(index));
        }
        return null;
    }

    public Photo past(){
        if (listPhotoId.equals(null)){
            listPhotoId = new ArrayList<>(photos.keySet());
            index = 0;
        }
        else {
            if (index == -1){
                index = listPhotoId.size() - 1;
            }
            else {
                index--;
            }
            return photos.get(listPhotoId.get(index));
        }
        return null;
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

        PhotoCollection photoCollection = null; // Инициализируем null
        try (FileReader reader = new FileReader(myFile)) {
            Gson gson = new Gson();
            photoCollection = gson.fromJson(reader, PhotoCollection.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (photoCollection != null) {
            List<Photo> photos = photoCollection.getPhotoCollection(); // Получаем список фотографий
            Iterator<Photo> iterator = photos.iterator();
            while (iterator.hasNext()) {
                Photo photo = iterator.next();
                // TODO: Удаляет записанные файлы
                File file = new File(context.getExternalFilesDir(null), photo.getPath()); // Используем context
                if (!file.exists()) {
                    iterator.remove(); // Безопасное удаление с помощью итератора
                }
            }

            photoCollection.ReadFileOnDevice();
        }

        return photoCollection;
    }

    public void ReadFileOnDevice(){
        File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File photoGalleryDir = new File(picturesDir, "PhotoGallery");

        if (photoGalleryDir.exists() && photoGalleryDir.isDirectory()) {
            File[] imageFiles = photoGalleryDir.listFiles();

            if (imageFiles != null && imageFiles.length > 0) {

                // Добавbnm все пути к файлам в список, если они еще не добавлены
                for (File imageFile : imageFiles) {
                    String path = imageFile.getAbsolutePath().toString();
                    if (this.getPhotoFromId(path) == null) {
                        // Если файл еще не добавлен, добавьте его в список
                        this.addPhoto(new Photo(path));
                    }
                }
            }
        }
        if (!photoGalleryDir.exists()) {
            boolean success = photoGalleryDir.mkdirs();
        }
    }
}
