package com.thresholdsoft.wakfboard.ui.mapdataliastactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.ActivityMapDataListBinding;
import com.thresholdsoft.wakfboard.ui.base.BaseActivity;
import com.thresholdsoft.wakfboard.ui.mapdataliastactivity.adapter.MapDataAdapter;
import com.thresholdsoft.wakfboard.ui.propertysurvey.PropertySurvey;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

public class MapDataListActivity extends BaseActivity implements MapDataListActivityMvpView {
    @Inject
    MapDataListActivityMvpPresenter<MapDataListActivityMvpView> mpresenter;
    ActivityMapDataListBinding activityMapDataListBinding;
    List<MapDataTable> mapDataTableList;
    int propertyId;
    MapDataAdapter mapDataAdapter;
    private static final int PROPERTY_SURVEY = 5435;


    public static Intent getStartIntent(Context context, int propertyId, String myJson) {
        Intent intent = new Intent(context, MapDataListActivity.class);
        intent.putExtra("propertyId", propertyId);
        intent.putExtra("mapDataTableListUnchecked", myJson);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMapDataListBinding = DataBindingUtil.setContentView(this, R.layout.activity_map_data_list);
        getActivityComponent().inject(this);
        mpresenter.onAttach(MapDataListActivity.this);
        setUp();
    }

    @Override
    protected void setUp() {
        activityMapDataListBinding.setCallback(mpresenter);
        if (getIntent() != null) {
            propertyId = (int) getIntent().getIntExtra("propertyId", 0);
            Gson gson = new Gson();
            String json = getIntent().getStringExtra("mapDataTableListUnchecked");
            Type type = new TypeToken<List<MapDataTable>>() {
            }.getType();
            if (mapDataTableList != null) {
                mapDataTableList.clear();
            }
            mapDataTableList = gson.fromJson(json, type);
        }

        mapDataAdapter = new MapDataAdapter(this, mapDataTableList, this);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityMapDataListBinding.mapDataRecycle.setLayoutManager(mLayoutManager2);
        activityMapDataListBinding.mapDataRecycle.setItemAnimator(new DefaultItemAnimator());
        activityMapDataListBinding.mapDataRecycle.setAdapter(mapDataAdapter);
        activityMapDataListBinding.mapDataRecycle.setNestedScrollingEnabled(false);
    }

    @Override
    public void anotherizedToken() {

    }

    private int position;

    @Override
    public void uncheckableData(int pos, List<MapDataTable> mapDataTable) {
        if (mapDataTableList.get(pos).isChecked()) {
            mapDataTableList.get(pos).setChecked(false);
        } else {
            mapDataTableList.get(pos).setChecked(true);
        }
        mapDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickSubmit() {
        Gson gson = new Gson();
        String myJson = gson.toJson(mapDataTableList);
        Intent intent = new Intent();
        intent.putExtra("mapDataTableListUnchecked", myJson);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        onClickSubmit();
    }

    private boolean isEditMode;

    @Override
    public void onClickEditMapView(int pos, List<MapDataTable> mapDataTable) {

        for (int i = 0; i < mapDataTableList.size(); i++) {
            if (i == pos) {
                mapDataTableList.get(pos).setEditable(true);
            } else {
                mapDataTableList.get(i).setEditable(false);
            }
        }

        isEditMode = true;

        Gson gson = new Gson();

        String myJson = gson.toJson(mapDataTableList);

        startActivityForResult(PropertySurvey.getStartIntent(MapDataListActivity.this, mapDataTable.get(pos).getMapType(), propertyId, myJson, pos, isEditMode), PROPERTY_SURVEY);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    String name;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PROPERTY_SURVEY:
                    if (data != null) {
                        String dialogName = (String) data.getSerializableExtra("dialogName");
                        name = dialogName;
                        Gson gson = new Gson();
                        String json = data.getStringExtra("mapDataTableListUnchecked");
                        Type type = new TypeToken<List<MapDataTable>>() {
                        }.getType();
                        mapDataTableList = gson.fromJson(json, type);

                        mpresenter.getMapTypelist(propertyId);

                        Gson gson1 = new Gson();
                        String myJson = gson1.toJson(mapDataTableList);
                        Intent intent = new Intent();
                        intent.putExtra("mapDataTableListUnchecked", myJson);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    break;
                default:
            }
        }
    }

}
