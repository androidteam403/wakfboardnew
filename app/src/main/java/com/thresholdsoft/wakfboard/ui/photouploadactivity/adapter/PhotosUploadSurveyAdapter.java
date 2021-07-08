package com.thresholdsoft.wakfboard.ui.photouploadactivity.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.AdapterPhotoUploadBinding;
import com.thresholdsoft.wakfboard.ui.photouploadactivity.PhotoUploadMvpView;

import java.io.File;
import java.util.List;

public class PhotosUploadSurveyAdapter extends RecyclerView.Adapter<PhotosUploadSurveyAdapter.ViewHolder> {
    private Context context;
    private List<String> multipleImageList;
    private PhotoUploadMvpView photoUploadMvpView;

    public PhotosUploadSurveyAdapter(Context context, List<String> multipleImageList, PhotoUploadMvpView photoUploadMvpView) {
        this.context = context;
        this.multipleImageList = multipleImageList;
        this.photoUploadMvpView = photoUploadMvpView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterPhotoUploadBinding adapterPhotoUploadBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.adapter_photo_upload, parent, false);
        return new ViewHolder(adapterPhotoUploadBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imagePath = multipleImageList.get(position);

        Glide.with(context).load(Uri.fromFile(new File(imagePath))).error(R.drawable.agri_logo).into(holder.adapterPhotoUploadBinding.imageView);

        holder.adapterPhotoUploadBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoUploadMvpView.onRemovePhoto(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return multipleImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterPhotoUploadBinding adapterPhotoUploadBinding;

        public ViewHolder(@NonNull AdapterPhotoUploadBinding adapterPhotoUploadBinding) {
            super(adapterPhotoUploadBinding.getRoot());
            this.adapterPhotoUploadBinding = adapterPhotoUploadBinding;
        }
    }

}