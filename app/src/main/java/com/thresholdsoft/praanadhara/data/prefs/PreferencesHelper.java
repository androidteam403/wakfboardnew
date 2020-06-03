package com.thresholdsoft.praanadhara.data.prefs;

import com.thresholdsoft.praanadhara.data.utils.LoggedInMode;
import com.thresholdsoft.praanadhara.ui.userlogin.model.LoginResponse;

public interface PreferencesHelper {
    void storeSurveyClick(boolean value);

    boolean isSurveyClick();

    void loginRequest(String req);

    String loginRequest();

    void loginResponse(String response);

    LoginResponse loginResponse();

    void storeUserLogin(boolean value);

    boolean isUserLogin();

    void storeUserNum(String key);

    String storeUserNum();

    void storeUserKey(String num);

    String storeUserKey();

    int getUserLoggedInMode();

    void setUserLoggedIn(LoggedInMode mode);

    Long getUserId();

    void setUserId(Long userId);

    String getUserName();

    void setUserName(String userName);

    String getUserEmail();

    void setUserEmail(String email);

    String getUserProfilePicUrl();

    void setUserProfilePicUrl(String profilePicUrl);

    String getAccessToken();

    void setAccessToken(String accessToken);

    String getUserMobile();

    void setUserMobile(String mobileNumber);

    boolean isCoachMarkView();

    void setCoachMarkView(boolean coachMark);

    boolean isFirstTime();

    void setFirstTime(boolean firstTime);

    void logoutUser();
}
