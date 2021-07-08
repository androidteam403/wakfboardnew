package com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.ActivitySurveyListBinding;
import com.thresholdsoft.wakfboard.ui.base.BaseFragment;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.adapter.SurveyAdapter;
import com.thresholdsoft.wakfboard.ui.propertycreation.PropertyCreation;
import com.thresholdsoft.wakfboard.ui.propertycreation.model.PropertyData;
import com.thresholdsoft.wakfboard.ui.propertysurveystatus.PropertyPreview;

import java.util.List;

import javax.inject.Inject;

public class SurveyListFrag extends BaseFragment implements SurveyListMvpView {
    @Inject
    SurveyListMvpPresenter<SurveyListMvpView> mpresenter;
    private ActivitySurveyListBinding activitySurveyListBinding;
    private int PROPERTY_CREATION = 1234;
    private SurveyAdapter surveyAdapter;
    private List<PropertyData> propertyDataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activitySurveyListBinding = DataBindingUtil.inflate(inflater, R.layout.activity_survey_list, container, false);
        getActivityComponent().inject(this);
        mpresenter.onAttach(SurveyListFrag.this);
        return activitySurveyListBinding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void setUp(View view) {

        this.propertyDataList = mpresenter.getPropertylist();

        activitySurveyListBinding.propertyCreationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(PropertyCreation.getStartIntent(getContext()), PROPERTY_CREATION);
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        activitySurveyListBinding.createProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(PropertyCreation.getStartIntent(getContext()), PROPERTY_CREATION);
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        if (propertyDataList != null && propertyDataList.size() > 0) {
            surveyAdapter = new SurveyAdapter(getContext(), propertyDataList, this);
            RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            activitySurveyListBinding.recyclerSurveyList.setLayoutManager(mLayoutManager2);
            activitySurveyListBinding.recyclerSurveyList.setItemAnimator(new DefaultItemAnimator());
            activitySurveyListBinding.recyclerSurveyList.setAdapter(surveyAdapter);
            activitySurveyListBinding.recyclerSurveyList.setNestedScrollingEnabled(false);
        } else {
            activitySurveyListBinding.noDataFound.setVisibility(View.VISIBLE);
            activitySurveyListBinding.recyclerSurveyList.setVisibility(View.GONE);
        }
        onSyncClick();
    }

    @Override
    public void anotherizedToken() {

    }

    @Override
    public void onItemClickTakeSurveyLister(int position) {
        Intent intent = new Intent(getContext(), PropertyPreview.class);
        intent.putExtra("propertyId", propertyDataList.get(position).getId());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private void onSyncClick() {
        ImageView imageView = getActivity().findViewById(R.id.refresh_sync);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (surveyAdapter != null) {
                    mpresenter.getPropertylist();
                    surveyAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}