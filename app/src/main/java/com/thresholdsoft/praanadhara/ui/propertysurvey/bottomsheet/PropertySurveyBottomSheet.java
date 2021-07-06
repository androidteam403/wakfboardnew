package com.thresholdsoft.praanadhara.ui.propertysurvey.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.BottomSheetPropertySurveyBinding;
import com.thresholdsoft.praanadhara.ui.propertysurvey.PropertySurveyMvpView;
import com.thresholdsoft.praanadhara.ui.propertysurveystatus.PropertySurveyStatusMvpView;

public class PropertySurveyBottomSheet extends BottomSheetDialogFragment implements PropertySurveyListener {
    BottomSheetPropertySurveyBinding bottomSheetPropertySurveyBinding;
    private int mapTypeData;
    PropertySurveyStatusMvpView propertySurveyStatusMvpView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        bottomSheetPropertySurveyBinding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_property_survey, container, false);
        setUP();
        return bottomSheetPropertySurveyBinding.getRoot();

    }

    private void setUP() {
        bottomSheetPropertySurveyBinding.setClicklisteners(this);
        if (bottomSheetPropertySurveyBinding.pointsRadio.isChecked()) {
            mapTypeData = 1;
            bottomSheetPropertySurveyBinding.linesRadio.setChecked(false);
            bottomSheetPropertySurveyBinding.polygonRadio.setChecked(false);
        }

        bottomSheetPropertySurveyBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onPointRadioClick() {
        mapTypeData = 1;
        bottomSheetPropertySurveyBinding.pointsRadio.setChecked(true);
        bottomSheetPropertySurveyBinding.linesRadio.setChecked(false);
        bottomSheetPropertySurveyBinding.polygonRadio.setChecked(false);
    }

    @Override
    public void onPolylineRadioClick() {
        mapTypeData = 2;
        bottomSheetPropertySurveyBinding.linesRadio.setChecked(true);
        bottomSheetPropertySurveyBinding.polygonRadio.setChecked(false);
        bottomSheetPropertySurveyBinding.pointsRadio.setChecked(false);
    }

    @Override
    public void onPolygonRadioClick() {
        mapTypeData = 3;
        bottomSheetPropertySurveyBinding.polygonRadio.setChecked(true);
        bottomSheetPropertySurveyBinding.linesRadio.setChecked(false);
        bottomSheetPropertySurveyBinding.pointsRadio.setChecked(false);
    }

    @Override
    public void onTakeSurveyClick() {
        dismiss();
        propertySurveyStatusMvpView.mapTypeData(mapTypeData);
    }

    public void setPresenterData(PropertySurveyStatusMvpView propertySurveyStatusMvpView) {
        this.propertySurveyStatusMvpView = propertySurveyStatusMvpView;
    }

//    public int mapTypeData() {
//        return mapTypeData;
//    }
}
