package Photo;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.photogallery.MainActivity;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import kotlin.jvm.Transient;

/**
 * Класс, представляющий коллекцию фотографий.
 * Хранит фотографии в виде {@code LinkedHashMap}, где ключом является идентификатор фотографии,
 * а значением - объект {@code Photo}.
 * Обеспечивает методы для добавления, удаления, поиска и сериализации фотографий.
 *
 * @author Nemo121007
 * @version 1.0
 * @since 2024-09-16
 */
public class PhotoCollection implements Serializable {
    /**
     * Коллекция фотографий, хранящаяся в виде {@code LinkedHashMap}.
     */
    @Expose
    private LinkedHashMap<String, Photo> photos;

    /**
     * Список идентификаторов фотографий для итерации.
     * Исключается из сериализации.
     */
    @Transient
    private List<String> listPhotoId;

    /**
     * Индекс текущей фотографии при итерации.
     * Исключается из сериализации.
     */
    @Transient
    private Integer index;

    /**
     * Конструктор по умолчанию.
     * Создает пустую коллекцию фотографий.
     */
    public PhotoCollection() {
        photos = new LinkedHashMap<>();
    }

    /**
     * Конструктор, принимающий массив фотографий.
     * Создает коллекцию и добавляет в нее фотографии из массива.
     *
     * @param photos Массив фотографий для добавления в коллекцию.
     */
    public PhotoCollection(@NonNull Photo... photos) {
        this();
        addPhotos(photos);
    }

    /**
     * Конструктор, принимающий список фотографий.
     * Создает коллекцию и добавляет в нее фотографии из списка.
     *
     * @param photos Список фотографий для добавления в коллекцию.
     */
    public PhotoCollection(@NonNull List<Photo> photos) {
        this();
        addPhotos(photos);
    }

    /**
     * Добавляет фотографию в коллекцию, если ее идентификатор еще не существует.
     *
     * @param photo Фотография для добавления.
     */
    public void addPhoto(@NonNull Photo photo) {
        if (!photos.containsKey(photo.getId())){
            photos.put(photo.getId(), photo);
        }
    }

    /**
     * Добавляет массив фотографий в коллекцию.
     *
     * @param photos Массив фотографий для добавления.
     */
    public void addPhotos(@NonNull Photo ... photos) {
        for (Photo photo : photos){
            if (photo != null){
                addPhoto(photo);
            }
        }
    }

    /**
     * Добавляет список фотографий в коллекцию.
     *
     * @param photos Список фотографий для добавления.
     */
    public void addPhotos(@NonNull List<Photo> photos) {
        for (Photo photo : photos){
            if (photo != null){
                addPhoto(photo);
            }
        }
    }

    /**
     * Удаляет фотографию из коллекции по объекту фотографии.
     *
     * @param photo Фотография для удаления.
     */
    public void removePhoto(@NonNull Photo photo) {
        removePhoto(photo.getId());
    }

    /**
     * Удаляет фотографию из коллекции по идентификатору.
     *
     * @param photoId Идентификатор фотографии для удаления.
     */
    public void removePhoto(String photoId) {
        photos.remove(photoId);

        File path = new File(MainActivity.getPhotoGalleryDir(), photoId);
        if (path.exists()) {
            path.delete();
        }
    }

    /**
     * Очищает коллекцию фотографий.
     */
    public void clear(){
        photos.clear();
    }

    /**
     * Возвращает список всех фотографий в коллекции.
     *
     * @return Список фотографий.
     */
    public List<Photo> getPhotoCollection() {
        return new ArrayList<>(photos.values());
    }

    /**
     * Возвращает фотографию по ее идентификатору.
     *
     * @param id Идентификатор фотографии.
     * @return Фотография с заданным идентификатором или {@code null}, если фотография не найдена.
     */
    public Photo getPhotoFromId(String id) {
        if (!photos.containsKey(id)){
            return null;
        }
        return photos.get(id);
    }

    /**
     * Возвращает список фотографий с заданным именем.
     *
     * @param name Имя фотографии.
     * @return Список фотографий с заданным именем.
     */
    public List<Photo> getPhotoFromName(String name) {
        List<Photo> result = new ArrayList<>();
        for (Photo photo : photos.values()) {
            if (photo.getName().equals(name)) {
                result.add(photo);
            }
        }
        return result;
    }

    /**
     * Возвращает список фотографий, описание которых содержит заданную подстроку.
     *
     * @param descryption Подстрока для поиска в описании.
     * @return Список фотографий, описание которых содержит заданную подстроку.
     */
    public List<Photo> getPhotoFromDescryption(String descryption) {
        List<Photo> result = new ArrayList<>();
        for (Photo photo : photos.values()) {
            if (photo.getDescryption().contains(descryption)) {
                result.add(photo);
            }
        }
        return result;
    }

    /**
     * Возвращает список фотографий, имеющих заданный тег.
     *
     * @param tag Тег для поиска.
     * @return Список фотографий, имеющих заданный тег.
     */
    public List<Photo> getPhotoFromTag(String tag) {
        List<Photo> result = new ArrayList<>();
        for (Photo photo : photos.values()) {
            if (photo.getTags().contains(tag)) {
                result.add(photo);
            }
        }
        return result;
    }

    /**
     * Возвращает следующую фотографию в коллекции.
     * Использует внутренний список идентификаторов для итерации.
     *
     * @return Следующая фотография или {@code null}, если достигнут конец коллекции.
     */
    public Photo next(String photoId) {
        if (listPhotoId == null){
            listPhotoId = new ArrayList<>(photos.keySet());
            index = listPhotoId.indexOf(photoId);
        }
        if (index == listPhotoId.size() - 1){
            index = 0;
        } else {
            index++;
        }
        return photos.get(listPhotoId.get(index));
    }

    /**
     * Возвращает предыдущую фотографию в коллекции.
     * Использует внутренний список идентификаторов для итерации.
     *
     * @return Предыдущая фотография или {@code null}, если достигнут начало коллекции.
     */
    public Photo past(){
        if (listPhotoId == null){
            listPhotoId = new ArrayList<>(photos.keySet());
            index = 0;
        } else {
            if (index == 0){
                index = listPhotoId.size() - 1;
            } else {
                index--;
            }
            return photos.get(listPhotoId.get(index));
        }
        return null;
    }

    /**
     * Сохраняет коллекцию фотографий в JSON файл во внутренней памяти устройства.
     *
     * @param context Контекст приложения.
     */
    public void WritePhotoCollection(@NonNull Context context) {
        File internalStorageDir = context.getFilesDir();
        File myFile = new File(internalStorageDir, "dataPhotoCollection.json");

        try (FileWriter writer = new FileWriter(myFile)) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(this);
            writer.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Читает коллекцию фотографий из JSON файла во внутренней памяти устройства.
     * В случае успешного чтения, проверяет наличие соответствующих файлов фотографий
     * в директории "PhotoGallery" на внешнем хранилище.
     * Если файл фотографии не найден, запись о нем удаляется из коллекции.
     * После этого вызывает метод {@code ReadFileOnDevice()} для добавления
     * новых фотографий из директории в коллекцию.
     * @param context Контекст приложения.
     * @return Коллекция фотографий, прочитанная из файла, или {@code null} в случае ошибки.
     */
    @Nullable
    public static PhotoCollection ReadPhotoCollection(@NonNull Context context) {
        File internalStorageDir = context.getFilesDir();
        File myFile = new File(internalStorageDir, "dataPhotoCollection.json");

        PhotoCollection photoCollection = null;
        try (FileReader reader = new FileReader(myFile)) {
            Gson gson = new Gson();
            photoCollection = gson.fromJson(reader, PhotoCollection.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (photoCollection != null) {
            List<Photo> photos = photoCollection.getPhotoCollection();
            Iterator<Photo> iterator = photos.iterator();

            File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File photoGalleryDir = new File(picturesDir, "PhotoGallery");
            while (iterator.hasNext()) {
                Photo photo = iterator.next();
                File file = new File(photoGalleryDir, Photo.GeneratorId(photo.getId()));
                if (!file.exists()) {
                    iterator.remove();
                }
            }

            photoCollection.ReadFileOnDevice();
        }

        return photoCollection;
    }

    /**
     * Считывает файлы фотографий из директории "PhotoGallery" на внешнем хранилище
     * и добавляет их в коллекцию, если они еще не присутствуют.
     * Если директория "PhotoGallery" не существует, создает ее.
     */
    public void ReadFileOnDevice(){
        File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File photoGalleryDir = new File(picturesDir, "PhotoGallery");

        if (photoGalleryDir.exists() && photoGalleryDir.isDirectory()) {
            File[] imageFiles = photoGalleryDir.listFiles();

            if (imageFiles != null && imageFiles.length > 0) {
                for (File imageFile : imageFiles) {
                    String path = Photo.GeneratorId(imageFile.getAbsolutePath());
                    if (this.getPhotoFromId(path) == null) {
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
