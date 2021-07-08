package com.thresholdsoft.wakfboard.ui.base;

/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
public interface MvpPresenter<V extends MvpView> {

    void onAttach(V mvpView);

    void onDetach();

    void handleApiError(Throwable error);

    void setUserAsLoggedOut();
}

