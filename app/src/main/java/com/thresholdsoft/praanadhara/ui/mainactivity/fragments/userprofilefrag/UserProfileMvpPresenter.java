package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.userprofilefrag;

import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;

public interface UserProfileMvpPresenter<V extends UserProfileMvpView> extends MvpPresenter<V> {
    String getUserName();

    String getUserContactNum();

    String getUserEmail();

    void onUpdateClick();

    void onCancelClick();
}
