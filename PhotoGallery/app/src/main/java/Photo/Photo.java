package Photo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Photo {
    @Expose
    private String Id;
    @Expose
    private String Name;
    @Expose
    private String Description;
    @Expose
    private Set<String> Tags;
    private String Expansion;

    public Photo(@NonNull String name) {
        Id = Integer.valueOf(name.hashCode()).toString();
        Description = "";
        Expansion = name;
        Tags = new HashSet<>();
        int dotIndex = name.indexOf('.');
        if (dotIndex != -1) {
            Name = name.substring(0, dotIndex);
            Expansion = ".*";
        } else {
            Name = name;
            Expansion = ""; // Или другое значение по умолчанию, если нет точки
        }
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public List<String> getTags() {
        return new ArrayList<>(Tags);
    }

    public void setTags(List<String> tags) {
        Tags = new HashSet<>(tags);
    }

    public void addTag(String tag) {
        Tags.add(tag);
    }

    public void removeTag(String tag) {
        Tags.remove(tag);
    }

    public void clearTags() {
        Tags.clear();
    }
}