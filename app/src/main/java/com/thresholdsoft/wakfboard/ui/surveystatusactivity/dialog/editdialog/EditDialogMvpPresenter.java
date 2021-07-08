package com.thresholdsoft.wakfboard.ui.surveystatusactivity.dialog.editdialog;

import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;

public interface EditDialogMvpPresenter<V extends EditDialogMvpView> extends MvpPresenter<V> {
    void onNoClick();

    void onYesClick();

}
