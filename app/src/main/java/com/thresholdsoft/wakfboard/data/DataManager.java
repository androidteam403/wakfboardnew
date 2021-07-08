package com.thresholdsoft.wakfboard.data;

import com.thresholdsoft.wakfboard.data.db.dao.SurveyDao;
import com.thresholdsoft.wakfboard.data.network.RestApiHelper;
import com.thresholdsoft.wakfboard.data.prefs.PreferencesHelper;

public interface DataManager extends SurveyDao, PreferencesHelper, RestApiHelper {
    void updateApiHeader(Long userId, String accessToken);

    void setUserLoggedOut();

    void updateUserInfo(
            String accessToken,
            String userName,
            String email,
            String phone);
}
