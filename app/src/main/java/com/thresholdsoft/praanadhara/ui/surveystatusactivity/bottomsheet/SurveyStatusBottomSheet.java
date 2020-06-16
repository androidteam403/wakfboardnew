package com.thresholdsoft.praanadhara.ui.surveystatusactivity.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.databinding.BottomSheetFragmentBinding;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpView;
import com.thresholdsoft.praanadhara.ui.surveytrack.SurveyTrackingActivity;

public class SurveyStatusBottomSheet extends BottomSheetDialogFragment implements ClickListener {
    BottomSheetFragmentBinding bottomSheetFragmentBinding;
    private SurveyStatusMvpView surveyStatusMvpView;
    private RowsEntity surveyModel;
    String uid;
    String landUid;
    int mapType;
    public static final int REQUEST_CODE = 2;
    private SurveyStatusBottomSheet bottomSheet;

    SurveyStatusMvpPresenter<SurveyStatusMvpView> mpresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        bottomSheetFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_fragment, container, false);
        setUP();
        return bottomSheetFragmentBinding.getRoot();

    }

    public void setUP() {

        Bundle bundle = getArguments();
        uid = bundle.getString("surveyDataSheet", "");
        landUid = bundle.getString("landUidSheet", "");
        mpresenter.getFarmerLand(uid, landUid).observe(getActivity(), farmerLands -> bottomSheetFragmentBinding.setFarmerLand(farmerLands));
        bottomSheetFragmentBinding.setClicklisteners(this);
        bottomSheetFragmentBinding.setPresenterCallback(mpresenter);

        mpresenter.getAllSurveyList(landUid).observe(getActivity(), surveyEntities -> {
            bottomSheetFragmentBinding.setSurvey(surveyEntities.size() > 0);
        });

        if (bottomSheetFragmentBinding.pointsRadio.isChecked()) {
            bottomSheetFragmentBinding.linesRadio.setChecked(false);
            bottomSheetFragmentBinding.polygonRadio.setChecked(false);
            mapType = 0;
        }

    }

    @Override
    public void onpolygonRadioClick() {
        bottomSheetFragmentBinding.polygonRadio.setChecked(true);
        bottomSheetFragmentBinding.linesRadio.setChecked(false);
        bottomSheetFragmentBinding.pointsRadio.setChecked(false);
        mapType = 2;
    }

    @Override
    public void onLinesRadioClick() {
        bottomSheetFragmentBinding.linesRadio.setChecked(true);
        bottomSheetFragmentBinding.polygonRadio.setChecked(false);
        bottomSheetFragmentBinding.pointsRadio.setChecked(false);
        mapType = 1;
    }

    @Override
    public void onPointsRadioClick() {
        bottomSheetFragmentBinding.pointsRadio.setChecked(true);
        bottomSheetFragmentBinding.linesRadio.setChecked(false);
        bottomSheetFragmentBinding.polygonRadio.setChecked(false);
        mapType = 0;
    }

    @Override
    public void addSurvey(FarmerLands rowsEntity) {
        dismiss();
        startActivityForResult(SurveyTrackingActivity.getIntent(getContext(), rowsEntity, mapType), REQUEST_CODE);
        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onCrossClick() {
        dismiss();
    }

    public void setPresenterData(SurveyStatusMvpPresenter<SurveyStatusMvpView> mpresenter) {
        this.mpresenter = mpresenter;
    }
}
