package com.thresholdsoft.wakfboard.ui.surveystatusactivity.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.thresholdsoft.wakfboard.BR;


public class SurveyDetailsModel extends BaseObservable {

    private String surveyName;
    private String surveyType;

    @Bindable
    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
        notifyPropertyChanged(BR.surveyName);
    }

    @Bindable
    public String getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
        notifyPropertyChanged(BR.surveyType);
    }
}
