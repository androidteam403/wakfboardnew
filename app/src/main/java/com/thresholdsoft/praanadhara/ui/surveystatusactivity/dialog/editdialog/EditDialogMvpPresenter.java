package com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.editdialog;

import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;

public interface EditDialogMvpPresenter<V extends EditDialogMvpView> extends MvpPresenter<V> {
    void onNoClick();

    void onYesClick();

}
