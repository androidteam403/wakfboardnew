package com.thresholdsoft.praanadhara.ui.surveystatusactivity.bottomsheet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.network.pojo.MapTypeEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.databinding.BottomSheetFragmentBinding;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpView;

public class SurveyStatusBottomSheet extends BottomSheetDialogFragment implements ClickListener{
    BottomSheetFragmentBinding bottomSheetFragmentBinding;
    private SurveyStatusMvpView surveyStatusMvpView;
    private RowsEntity surveyModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        bottomSheetFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_fragment, container, false);
        setUP();
        return bottomSheetFragmentBinding.getRoot();

    }

    public void setSurveyStatusMvpView(SurveyStatusMvpView surveyStatusMvpView) {
        this.surveyStatusMvpView = surveyStatusMvpView;
    }
    public void setUP(){

        Intent i=getActivity().getIntent();
        surveyModel = (RowsEntity) i.getSerializableExtra("surveyData");


        if (bottomSheetFragmentBinding.pointsRadio.isChecked()) {
            bottomSheetFragmentBinding.linesRadio.setChecked(false);
            bottomSheetFragmentBinding.polygonRadio.setChecked(false);
            surveyModel.setSurveyType(0);
            MapTypeEntity mapTypeEntity = new MapTypeEntity();
            mapTypeEntity.setUid("point");
            mapTypeEntity.setName("point");
            surveyModel.setMapTypeEntity(mapTypeEntity);
        }

    }

    @Override
    public void onpolygonRadioClick() {
        bottomSheetFragmentBinding.polygonRadio.setChecked(true);
        bottomSheetFragmentBinding.linesRadio.setChecked(false);
        bottomSheetFragmentBinding.pointsRadio.setChecked(false);
        surveyModel.setSurveyType(2);
        MapTypeEntity mapTypeEntity = new MapTypeEntity();
        mapTypeEntity.setUid("polygon");
        mapTypeEntity.setName("polygon");
        surveyModel.setMapTypeEntity(mapTypeEntity);
    }

    @Override
    public void onLinesRadioClick() {
        bottomSheetFragmentBinding.linesRadio.setChecked(true);
        bottomSheetFragmentBinding.polygonRadio.setChecked(false);
        bottomSheetFragmentBinding.pointsRadio.setChecked(false);
        surveyModel.setSurveyType(1);
        MapTypeEntity mapTypeEntity = new MapTypeEntity();
        mapTypeEntity.setUid("line");
        mapTypeEntity.setName("line");
        surveyModel.setMapTypeEntity(mapTypeEntity);
    }

    @Override
    public void onPointsRadioClick() {
        bottomSheetFragmentBinding.pointsRadio.setChecked(true);
        bottomSheetFragmentBinding.linesRadio.setChecked(false);
        bottomSheetFragmentBinding.polygonRadio.setChecked(false);
        surveyModel.setSurveyType(0);
        MapTypeEntity mapTypeEntity = new MapTypeEntity();
        mapTypeEntity.setUid("point");
        mapTypeEntity.setName("point");
        surveyModel.setMapTypeEntity(mapTypeEntity);
    }

    @Override
    public void addSurvey(RowsEntity rowsEntity) {

    }

    @Override
    public void submitSurvey(RowsEntity rowsEntity) {

    }

    @Override
    public void startSurvey(RowsEntity surveyModel) {

    }
}
