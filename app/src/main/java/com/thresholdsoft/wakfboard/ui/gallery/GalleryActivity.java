package com.thresholdsoft.wakfboard.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.ActivityGalleryBinding;
import com.thresholdsoft.wakfboard.ui.alertdialog.CutomAlertBox;
import com.thresholdsoft.wakfboard.ui.base.BaseActivity;
import com.thresholdsoft.wakfboard.ui.gallery.adapter.GalleryAdapter;
import com.thresholdsoft.wakfboard.ui.propertycreation.model.PropertyData;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GalleryActivity extends BaseActivity implements GalleryMvpView {
    @Inject
    GalleryMvpPresenter<GalleryMvpView> mPresenter;
    private ActivityGalleryBinding galleryBinding;
    private int propertyId;
    private List<MapDataTable> mapDataTableList;
    private List<String> imagePathList = new ArrayList<>();
    private GalleryAdapter galleryAdapter;
    private int position;
    private boolean individual;
    private List<PropertyData> propertyDataList = new ArrayList<>();
    private int id;

    public static Intent getStartIntent(Context context, int propertyId, int id) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra("propertyId", propertyId);
        intent.putExtra("id", id);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    public static Intent getStartIntent(Context context, int propertyId, int pos, boolean individualGallery, String myJson) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra("propertyId", propertyId);
        intent.putExtra("pos", pos);
        intent.putExtra("individual", individualGallery);
        intent.putExtra("mapDataTableListUnchecked", myJson);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        galleryBinding = DataBindingUtil.setContentView(this, R.layout.activity_gallery);
        getActivityComponent().inject(this);
        mPresenter.onAttach(GalleryActivity.this);
        setUp();
    }

    @Override
    protected void setUp() {
        galleryBinding.setCallback(mPresenter);
        if (getIntent() != null) {
            propertyId = (int) getIntent().getIntExtra("propertyId", 0);
            position = (int) getIntent().getIntExtra("pos", 0);
            individual = (boolean) getIntent().getBooleanExtra("individual", false);
            id = (int) getIntent().getIntExtra("id", 0);
        }
        this.mapDataTableList = mPresenter.getAllMapDataTableListByPropertyid(propertyId);
        this.propertyDataList = mPresenter.getAllPropertyList(id);
//        if (mapDataTableList.size() > 0) {
//            for (int i = 0; i < mapDataTableList.size(); i++) {
//                    this.mapDataTableList.get(i).setPropertyListData(mPresenter.getAllPropertyList(id).get(0).getPhotosList());
//            }
//        }
        if (mPresenter.getAllPropertyList(propertyId).get(0).getPhotosList().size() > 0) {
            imagePathList.addAll(mPresenter.getAllPropertyList(propertyId).get(0).getPhotosList());
        }
//        this.mapDataTableList.addAll(mPresenter.getAllPropertyList());

        if (individual) {
            imagePathList = mapDataTableList.get(position).getPointPhotoData();
            if (imagePathList != null && imagePathList.size() > 0) {
                galleryAdapter = new GalleryAdapter(this, imagePathList, this, true, true);
                galleryBinding.galleryListRecycler.setLayoutManager(new GridLayoutManager(this, 3));
                galleryBinding.galleryListRecycler.setAdapter(galleryAdapter);
                galleryBinding.setIsGallery(true);
                galleryBinding.setIndividual(true);
            } else {
                galleryBinding.setIsGallery(false);
                galleryBinding.setIndividual(true);
            }
        } else {
            if (mapDataTableList != null && mapDataTableList.size() > 0) {
                for (MapDataTable mapDataTable : mapDataTableList) {
                    imagePathList.addAll(mapDataTable.getPointPhotoData());
                    if (mapDataTable.getPropertyListData() != null && mapDataTable.getPropertyListData().size() > 0) {
                        imagePathList.addAll(mapDataTable.getPropertyListData());
                    }
                }
            }
            if (imagePathList != null && imagePathList.size() > 0) {
                galleryAdapter = new GalleryAdapter(this, imagePathList, this, false, false);
                galleryBinding.galleryListRecycler.setLayoutManager(new GridLayoutManager(this, 3));
                galleryBinding.galleryListRecycler.setAdapter(galleryAdapter);
                galleryBinding.setIsGallery(true);
                galleryBinding.setIndividual(false);
            } else {
                galleryBinding.setIsGallery(false);
                galleryBinding.setIndividual(false);
            }
        }

        galleryBinding.galleryAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePicker.Builder(GalleryActivity.this)
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .scale(600, 600)
                        .allowMultipleImages(true)
                        .enableDebuggingMode(true)
                        .build();
            }
        });
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

            imagePathList.addAll(mPaths);

            mapDataTableList.get(position).setPointPhotoData(imagePathList);

            mPresenter.updateMapDataList(mapDataTableList.get(position));

            //Your Code

            galleryAdapter = new GalleryAdapter(this, imagePathList, this, true, false);
            galleryBinding.galleryListRecycler.setLayoutManager(new GridLayoutManager(this, 3));
            galleryBinding.galleryListRecycler.setAdapter(galleryAdapter);
            galleryBinding.setIsGallery(true);


        }
    }

    @Override
    public void anotherizedToken() {

    }

    @Override
    public void onBackPressed() {
        onClickBack();
    }

    @Override
    public void onClickBack() {
        if (galleryBinding.fullView.getVisibility() == View.VISIBLE) {
            galleryBinding.fullView.setVisibility(View.GONE);
            galleryBinding.imageFullviewDelete.setVisibility(View.GONE);
            galleryBinding.galleryListRecycler.setVisibility(View.VISIBLE);
        } else {
            Intent intent = new Intent();
            if (mapDataTableList != null && mapDataTableList.size() > 0) {
                Gson gson = new Gson();
                String myJson = gson.toJson(mapDataTableList);
                intent.putExtra("mapDataTableListUnchecked", myJson);
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onImageClick(int position, String path) {
        galleryBinding.galleryListRecycler.setVisibility(View.GONE);
        galleryBinding.fullView.setVisibility(View.VISIBLE);
        galleryBinding.imageFullviewDelete.setVisibility(View.VISIBLE);
        Glide.with(this).load(path).into(galleryBinding.fullView);
        galleryBinding.imageFullviewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryBinding.fullView.setVisibility(View.GONE);
                galleryBinding.imageFullviewDelete.setVisibility(View.GONE);
                galleryBinding.galleryListRecycler.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onGalleryDeleteClick(int pos) {
        CutomAlertBox cutomAlertBox = new CutomAlertBox(GalleryActivity.this);

        cutomAlertBox.setTitle("Do you want to delete image ?");
        cutomAlertBox.setPositiveListener(view -> {
            imagePathList.remove(pos);
            galleryAdapter.notifyDataSetChanged();
            mapDataTableList.get(this.position).setPointPhotoData(imagePathList);
            mPresenter.updateMapDataList(mapDataTableList.get(this.position));
            cutomAlertBox.dismiss();
            Toast.makeText(this, "Image removed successfully", Toast.LENGTH_SHORT).show();
        });
        cutomAlertBox.setNegativeListener(v -> cutomAlertBox.dismiss());
        cutomAlertBox.show();


    }

//    public class MergedDataList {
//
//        private List<MapDataTable> mapDataTableList;
//        private List<PropertyData> propertyDataList;
//
//        public List<MapDataTable> getMapDataTableList() {
//            return mapDataTableList;
//        }
//
//        public void setMapDataTableList(List<MapDataTable> mapDataTableList) {
//            this.mapDataTableList = mapDataTableList;
//        }
//
//        public List<PropertyData> getPropertyDataList() {
//            return propertyDataList;
//        }
//
//        public void setPropertyDataList(List<PropertyData> propertyDataList) {
//            this.propertyDataList = propertyDataList;
//        }
//    }
}
