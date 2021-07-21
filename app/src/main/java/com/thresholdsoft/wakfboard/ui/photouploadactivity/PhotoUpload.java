package com.thresholdsoft.wakfboard.ui.photouploadactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.ActivityPhotoUploadBinding;
import com.thresholdsoft.wakfboard.ui.alertdialog.CutomAlertBox;
import com.thresholdsoft.wakfboard.ui.base.BaseActivity;
import com.thresholdsoft.wakfboard.ui.gallery.GalleryActivity;
import com.thresholdsoft.wakfboard.ui.photouploadactivity.adapter.PhotosUploadSurveyAdapter;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PhotoUpload extends BaseActivity implements PhotoUploadMvpView {
    @Inject
    PhotoUploadMvpPresenter<PhotoUploadMvpView> mpresenter;
    ActivityPhotoUploadBinding activityPhotoUploadBinding;
    private PhotosUploadSurveyAdapter photosUploadAdapter;
    private List<String> mPaths = new ArrayList<>();
    public final static String IMAGEA_LIST = "IMAGEA_LIST";

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, PhotoUpload.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPhotoUploadBinding = DataBindingUtil.setContentView(this, R.layout.activity_photo_upload);
        getActivityComponent().inject(this);
        mpresenter.onAttach(PhotoUpload.this);
        setUp();
    }

    @Override
    protected void setUp() {
        activityPhotoUploadBinding.setCallBack(mpresenter);
        if (getIntent() != null) {
            mPaths = (List<String>) getIntent().getSerializableExtra(IMAGEA_LIST);
        }
        if (mPaths != null && mPaths.size() > 0) {
            activityPhotoUploadBinding.noDataFound.setVisibility(View.GONE);
            activityPhotoUploadBinding.phoRecycle.setVisibility(View.VISIBLE);
            photosUploadAdapter = new PhotosUploadSurveyAdapter(this, mPaths, this);
            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
            activityPhotoUploadBinding.phoRecycle.setLayoutManager(new GridLayoutManager(this, 3));
            activityPhotoUploadBinding.phoRecycle.setAdapter(photosUploadAdapter);
        } else {
            activityPhotoUploadBinding.noDataFound.setVisibility(View.VISIBLE);
            activityPhotoUploadBinding.phoRecycle.setVisibility(View.GONE);
        }
    }

    @Override
    public void anotherizedToken() {

    }

    @Override
    public void photoUploadButton() {
        new ImagePicker.Builder(PhotoUpload.this)
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .scale(600, 600)
                .allowMultipleImages(true)
                .enableDebuggingMode(true)
                .build();
    }

    @Override
    public void onRemovePhoto(int position) {

        CutomAlertBox cutomAlertBox = new CutomAlertBox(PhotoUpload.this);

        cutomAlertBox.setTitle("Do you want to delete image ?");
        cutomAlertBox.setPositiveListener(view -> {
            mPaths.remove(position);
            photosUploadAdapter.notifyDataSetChanged();
            if (mPaths != null && mPaths.size() > 0) {
                activityPhotoUploadBinding.phoRecycle.setVisibility(View.VISIBLE);
                activityPhotoUploadBinding.noDataFound.setVisibility(View.GONE);
            } else {
                activityPhotoUploadBinding.phoRecycle.setVisibility(View.GONE);
                activityPhotoUploadBinding.noDataFound.setVisibility(View.VISIBLE);
            }
            cutomAlertBox.dismiss();
            Toast.makeText(this, "Image removed successfully", Toast.LENGTH_SHORT).show();
        });
        cutomAlertBox.setNegativeListener(v -> cutomAlertBox.dismiss());
        cutomAlertBox.show();



    }

    @Override
    public void imagePathFullView(int pos, String path) {
        activityPhotoUploadBinding.parent.setVisibility(View.GONE);
        activityPhotoUploadBinding.fullView.setVisibility(View.VISIBLE);
        activityPhotoUploadBinding.imageFullviewDelete.setVisibility(View.VISIBLE);
        Glide.with(this).load(path).into(activityPhotoUploadBinding.fullView);
        activityPhotoUploadBinding.imageFullviewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityPhotoUploadBinding.fullView.setVisibility(View.GONE);
                activityPhotoUploadBinding.imageFullviewDelete.setVisibility(View.GONE);
                activityPhotoUploadBinding.parent.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onBackClickData() {
        onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPathsDummy = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            if (mPaths != null) {
                if (mPathsDummy != null && mPathsDummy.size() > 0) {
                    for (String path : mPathsDummy) {
                        mPaths.add(path);
                    }
                }
            } else {
                mPaths = new ArrayList<>();
                if (mPathsDummy != null && mPathsDummy.size() > 0) {
                    for (String path : mPathsDummy) {
                        mPaths.add(path);
                    }
                }
            }

            //Your Code
            if (mPaths != null && mPaths.size() > 0) {
                activityPhotoUploadBinding.noDataFound.setVisibility(View.GONE);
                activityPhotoUploadBinding.phoRecycle.setVisibility(View.VISIBLE);
                photosUploadAdapter = new PhotosUploadSurveyAdapter(this, mPaths, this);
                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
                activityPhotoUploadBinding.phoRecycle.setLayoutManager(new GridLayoutManager(this, 3));
                activityPhotoUploadBinding.phoRecycle.setAdapter(photosUploadAdapter);
            } else {
                activityPhotoUploadBinding.noDataFound.setVisibility(View.VISIBLE);
                activityPhotoUploadBinding.phoRecycle.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (activityPhotoUploadBinding.fullView.getVisibility() == View.VISIBLE) {
            activityPhotoUploadBinding.fullView.setVisibility(View.GONE);
            activityPhotoUploadBinding.imageFullviewDelete.setVisibility(View.GONE);
            activityPhotoUploadBinding.parent.setVisibility(View.VISIBLE);
        } else {
            Intent intent = new Intent();
            intent.putExtra("mpaths", (Serializable) mPaths);
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.left_right, R.anim.right_left);
        }
    }
}
