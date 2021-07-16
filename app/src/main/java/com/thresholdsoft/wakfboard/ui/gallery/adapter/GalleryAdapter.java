package com.thresholdsoft.wakfboard.ui.gallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.AdapterGalleryBinding;
import com.thresholdsoft.wakfboard.ui.gallery.GalleryMvpView;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context context;
    private List<String> imagePathList;
    private GalleryMvpView galleryMvpView;
    private boolean deleteVisibility;

    public GalleryAdapter(Context context, List<String> imagePathList, GalleryMvpView galleryMvpView,boolean deleteVisibility) {
        this.context = context;
        this.imagePathList = imagePathList;
        this.galleryMvpView = galleryMvpView;
        this.deleteVisibility=deleteVisibility;
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
        if (deleteVisibility){
            holder.galleryBinding.delete.setVisibility(View.VISIBLE);
        }else {
            holder.galleryBinding.delete.setVisibility(View.GONE);
        }


        holder.galleryBinding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (galleryMvpView != null) {
                    galleryMvpView.onImageClick(position);
                }
            }
        });
        holder.galleryBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (galleryMvpView != null) {
                    galleryMvpView.onGalleryDeleteClick(position);
                }
            }
        });
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
