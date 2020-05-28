package com.thresholdsoft.praanadhara.ui.login;

import com.thresholdsoft.praanadhara.data.network.pojo.UserProfile;
import com.thresholdsoft.praanadhara.ui.base.MvpView;

public interface LoginMvpView extends MvpView {
    void onLoginSuccess(UserProfile mUser);

    String getEmail();

    String getPassword();

    void showInputError();

    void setPassword(String userId);

    void setEmail(String password);
}
