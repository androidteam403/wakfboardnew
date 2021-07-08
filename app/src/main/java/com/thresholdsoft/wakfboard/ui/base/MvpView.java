package com.thresholdsoft.wakfboard.ui.base;

import androidx.annotation.StringRes;

/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
public interface MvpView {

    void showLoading();

    void hideLoading();

    void openActivityOnTokenExpire();

    void openLoginActivity();

    void onError(@StringRes int resId);

    void onError(String message);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    boolean isNetworkConnected();

    void hideKeyboard();

    void anotherizedToken();

}

