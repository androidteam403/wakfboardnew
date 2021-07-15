package com.thresholdsoft.wakfboard.ui.gallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.AdapterGalleryBinding;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context context;
    private List<String> imagePathList;

    public GalleryAdapter(Context context, List<String> imagePathList) {
        this.context = context;
        this.imagePathList = imagePathList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterGalleryBinding galleryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_gallery, parent, false);
        return new GalleryAdapter.ViewHolder(galleryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imagePath = imagePathList.get(position);
        Glide.with(context).load(imagePath).into(holder.galleryBinding.image);
    }

    @Override
    public int getItemCount() {
        return imagePathList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AdapterGalleryBinding galleryBinding;

        public ViewHolder(@NonNull AdapterGalleryBinding galleryBinding) {
            super(galleryBinding.getRoot());
            this.galleryBinding = galleryBinding;
        }
    }
}
