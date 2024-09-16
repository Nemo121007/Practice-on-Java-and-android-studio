package Photo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Класс, представляющий фотографию в галерее.
 * Содержит информацию об идентификаторе, имени, описании и тегах фотографии.
 *
 * @author Nemo121007
 * @version 1.0
 * @since 2024-09-16
 */
public class Photo implements Serializable {
    /**
     * Идентификатор фотографии.
     */
    @Expose
    private final String Id;

    /**
     * Имя фотографии.
     */
    @Expose
    private String Name;

    /**
     * Описание фотографии.
     */
    @Expose
    private String Description;

    /**
     * Множество тегов, связанных с фотографией.
     */
    @Expose
    private Set<String> Tags;

    /**
     * Конструктор класса {@code Photo}.
     * Создает новый объект фотографии с заданным идентификатором.
     * Инициализирует имя и описание пустыми строками, а множество тегов - пустым множеством.
     *
     * @param id Идентификатор фотографии (например, имя файла).
     */
    public Photo(@NonNull String id) {
        Id = Photo.GeneratorId(id);
        Name = "";
        Description = "";
        Tags = new HashSet<>();
    }

    /**
     * Статический метод для генерации идентификатора фотографии из имени файла.
     * Извлекает имя файла из полного пути и возвращает его в качестве идентификатора.
     *
     * @param name Полный путь к файлу фотографии.
     * @return Идентификатор фотографии (имя файла).
     */
    @NonNull
    public static String GeneratorId(@NonNull String name){
        int i = name.lastIndexOf('/');
        return name.substring( i + 1); // +1 чтобы не включать сам символ '/'
    }

    /**
     * Возвращает идентификатор фотографии.
     *
     * @return Идентификатор фотографии.
     */
    public String getId() {
        return Id;
    }

    /**
     * Возвращает имя фотографии.
     *
     * @return Имя фотографии.
     */
    public String getName() {
        return Name;
    }

    /**
     * Устанавливает имя фотографии.
     *
     * @param name Новое имя фотографии.
     */
    public void setName(String name) {
        Name = name;
    }

    /**
     * Возвращает описание фотографии.
     *
     * @return Описание фотографии.
     */
    public String getDescryption() {
        return Description;
    }

    /**
     * Устанавливает описание фотографии.
     *
     * @param description Новое описание фотографии.
     */
    public void setDescription(String description) {
        Description = description;
    }

    /**
     * Возвращает список тегов, связанных с фотографией.
     *
     * @return Список тегов фотографии.
     */
    public List<String> getTags() {
        return new ArrayList<>(Tags);
    }

    /**
     * Устанавливает множество тегов для фотографии.
     *
     * @param tags Новое множество тегов.
     */
    public void setTags(HashSet<String> tags) {
        Tags = new HashSet<>(tags);
    }

    /**
     * Добавляет тег к фотографии, если он еще не существует.
     *
     * @param tag Тег для добавления.
     */
    public void addTag(String tag) {
        Tags.add(tag);
    }

    /**
     * Удаляет тег из фотографии.
     *
     * @param tag Тег для удаления.
     */
    public void removeTag(String tag) {
        Tags.remove(tag);
    }

    /**
     * Удаляет все теги из фотографии.
     */
    public void clearTags() {
        Tags.clear();
    }
}