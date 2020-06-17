package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.thresholdsoft.praanadhara.BR;

import java.util.ArrayList;

public class DataModel extends BaseObservable {

    private ArrayList<SurveyListModel> modelArrayList = new ArrayList<>();

    @Bindable
    public ArrayList<SurveyListModel> getModelArrayList() {
        return modelArrayList;
    }

    public void setModelArrayList(ArrayList<SurveyListModel> modelArrayList) {
        this.modelArrayList = modelArrayList;
        notifyPropertyChanged(BR.modelArrayList);
    }
}
