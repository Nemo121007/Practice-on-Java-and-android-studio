package Photo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Photo implements Serializable {
    @Expose
    private String Id;
    @Expose
    private String Name;
    @Expose
    private String Description;
    @Expose
    private Set<String> Tags;

    public Photo(@NonNull String id) {
        Id = id;
        Name = "";
        Description = "";
        Tags = new HashSet<>();
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getPath(){
        return getId();
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescryption() {
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