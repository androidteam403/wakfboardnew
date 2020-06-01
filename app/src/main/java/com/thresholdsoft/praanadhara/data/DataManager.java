package com.thresholdsoft.praanadhara.data;

import com.thresholdsoft.praanadhara.data.db.dao.SurveyDao;
import com.thresholdsoft.praanadhara.data.network.RestApiHelper;
import com.thresholdsoft.praanadhara.data.prefs.PreferencesHelper;
import com.thresholdsoft.praanadhara.data.utils.LoggedInMode;

public interface DataManager extends SurveyDao, PreferencesHelper, RestApiHelper {
    void updateApiHeader(Long userId, String accessToken);

    void setUserLoggedOut();

    void updateUserInfo(
            String accessToken,
            Long userId,
            LoggedInMode loggedInMode,
            String userName,
            String email,
            String profilePicPath);
}
