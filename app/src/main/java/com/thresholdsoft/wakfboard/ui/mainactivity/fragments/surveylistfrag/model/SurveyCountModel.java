package com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.thresholdsoft.wakfboard.BR;

public class SurveyCountModel extends BaseObservable {

    private int newCount;
    private int inProgressCount;
    private int completedCount;

    @Bindable
    public int getNewCount() {
        return newCount;
    }

    public void setNewCount(int newCount) {
        this.newCount = newCount;
        notifyPropertyChanged(BR.newCount);
    }

    @Bindable
    public int getInProgressCount() {
        return inProgressCount;
    }

    public void setInProgressCount(int inProgressCount) {
        this.inProgressCount = inProgressCount;
        notifyPropertyChanged(BR.inProgressCount);
    }

    @Bindable
    public int getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
        notifyPropertyChanged(BR.completedCount);
    }
}
