package com.thresholdsoft.wakfboard.ui.photouploadactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.ActivityPhotoUploadBinding;
import com.thresholdsoft.wakfboard.ui.base.BaseActivity;
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
    }

    @Override
    public void anotherizedToken() {

    }

    @Override
    public void photoUploadButton() {
        activityPhotoUploadBinding.uploadButton.setVisibility(View.GONE);

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
        mPaths.remove(position);
        photosUploadAdapter.notifyDataSetChanged();
        if (mPaths.size() < 1) {
            activityPhotoUploadBinding.uploadButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackClickData() {
        onBackPressed();
    }

    private List<String> mPaths = new ArrayList<>();

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

            photosUploadAdapter = new PhotosUploadSurveyAdapter(this, mPaths, this);
            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
            activityPhotoUploadBinding.phoRecycle.setLayoutManager(new GridLayoutManager(this, 3));
            activityPhotoUploadBinding.phoRecycle.setAdapter(photosUploadAdapter);


        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("mpaths", (Serializable) mPaths);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
