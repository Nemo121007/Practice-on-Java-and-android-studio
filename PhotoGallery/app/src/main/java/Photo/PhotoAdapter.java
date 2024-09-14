package Photo;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photogallery.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private List<Photo> photos;

    public PhotoAdapter(List<Photo> photosList) {
        if (photosList != null){
            this.photos = photosList;
        }
        else {
            this.photos = new ArrayList<>();
        }
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_item, parent, false);
        return new PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);
        String path = photo.getPath();
        Glide.with(holder.itemView.getContext())
                .load(path)
                .placeholder(R.drawable.question)
                .error(R.drawable.question)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
