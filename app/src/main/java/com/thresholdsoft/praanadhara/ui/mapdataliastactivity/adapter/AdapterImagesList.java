package com.thresholdsoft.praanadhara.ui.mapdataliastactivity.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.AdapterImagesListBinding;
import com.thresholdsoft.praanadhara.databinding.AdapterMapDataListBinding;
import com.thresholdsoft.praanadhara.ui.photouploadactivity.PhotoUploadMvpView;

import java.io.File;
import java.util.List;

public class AdapterImagesList extends RecyclerView.Adapter<AdapterImagesList.ViewHolder> {
    private Context context;
    private List<String> multipleImageList;
    private PhotoUploadMvpView photoUploadMvpView;

    public AdapterImagesList(Context context, List<String> multipleImageList) {
        this.context = context;
        this.multipleImageList = multipleImageList;
        this.photoUploadMvpView = photoUploadMvpView;
    }

    @NonNull
    @Override
    public AdapterImagesList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterImagesListBinding adapterImagesListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.adapter_images_list, parent, false);
        return new AdapterImagesList.ViewHolder(adapterImagesListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterImagesList.ViewHolder holder, int position) {
        String imagePath = multipleImageList.get(position);

        Glide.with(context).load(Uri.fromFile(new File(imagePath))).error(R.drawable.agri_logo).into(holder.adapterImagesListBinding.imageView);

//        holder.adapterMapDataListBinding.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                photoUploadMvpView.onRemovePhoto(position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return multipleImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterImagesListBinding adapterImagesListBinding;

        public ViewHolder(@NonNull AdapterImagesListBinding adapterImagesListBinding) {
            super(adapterImagesListBinding.getRoot());
            this.adapterImagesListBinding = adapterImagesListBinding;
        }
    }

}