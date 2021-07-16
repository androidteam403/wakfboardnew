package com.thresholdsoft.wakfboard.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.ActivityGalleryBinding;
import com.thresholdsoft.wakfboard.ui.base.BaseActivity;
import com.thresholdsoft.wakfboard.ui.gallery.adapter.GalleryAdapter;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;

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

    public static Intent getStartIntent(Context context, int propertyId) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra("propertyId", propertyId);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    public static Intent getStartIntent(Context context, int propertyId, int pos, boolean individualGallery) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra("propertyId", propertyId);
        intent.putExtra("pos", pos);
        intent.putExtra("individual", individualGallery);
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
        }
        this.mapDataTableList = mPresenter.getAllMapDataTableListByPropertyid(propertyId);
        if (individual) {
            if (imagePathList != null && imagePathList.size() > 0) {
                imagePathList = mapDataTableList.get(position).getPointPhotoData();
                galleryAdapter = new GalleryAdapter(this, imagePathList);
                galleryBinding.galleryListRecycler.setLayoutManager(new GridLayoutManager(this, 3));
                galleryBinding.galleryListRecycler.setAdapter(galleryAdapter);
                galleryBinding.setIsGallery(true);
            } else {
                galleryBinding.setIsGallery(false);
            }
        } else {
            if (mapDataTableList != null && mapDataTableList.size() > 0) {
                for (MapDataTable mapDataTable : mapDataTableList) {
                    imagePathList.addAll(mapDataTable.getPointPhotoData());
                }
            }
            if (imagePathList != null && imagePathList.size() > 0) {
                galleryAdapter = new GalleryAdapter(this, imagePathList);
                galleryBinding.galleryListRecycler.setLayoutManager(new GridLayoutManager(this, 3));
                galleryBinding.galleryListRecycler.setAdapter(galleryAdapter);
                galleryBinding.setIsGallery(true);
            } else {
                galleryBinding.setIsGallery(false);
            }
        }
    }

    @Override
    public void anotherizedToken() {

    }

    @Override
    public void onClickBack() {
        onBackPressed();
    }
}
