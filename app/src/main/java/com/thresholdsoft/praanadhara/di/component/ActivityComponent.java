package com.thresholdsoft.praanadhara.di.component;


import com.thresholdsoft.praanadhara.di.PerActivity;
import com.thresholdsoft.praanadhara.di.module.ActivityModule;
import com.thresholdsoft.praanadhara.ui.mainactivity.MainActiivty;
import com.thresholdsoft.praanadhara.ui.mainactivity.dialog.LogoutDialog;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.newenrollmentfrag.NewEnrollmentFrag;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.SurveyListFrag;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.userprofilefrag.UserProfileFragment;
import com.thresholdsoft.praanadhara.ui.photouploadactivity.PhotoUpload;
import com.thresholdsoft.praanadhara.ui.propertycreation.PropertyCreation;
import com.thresholdsoft.praanadhara.ui.propertysurvey.PropertySurvey;
import com.thresholdsoft.praanadhara.ui.propertysurveystatus.PropertySurveyStatus;
import com.thresholdsoft.praanadhara.ui.selectingformactivity.SelectingFormActivity;
import com.thresholdsoft.praanadhara.ui.splash.SplashActivity;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusActivity;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.deletedialog.DeleteDialog;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.editdialog.EditDialog;
import com.thresholdsoft.praanadhara.ui.surveytrack.SurveyTrackingActivity;
import com.thresholdsoft.praanadhara.ui.userlogin.UserLoginActivity;

import dagger.Component;

/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashActivity splashActivity);

    void inject(SelectingFormActivity selectingFormActivity);

    void inject(UserLoginActivity userLoginActivity);

    void inject(MainActiivty mainActiivty);

    void inject(UserProfileFragment userProfileFragment);

    void inject(NewEnrollmentFrag newEnrollmentFrag);

    void inject(SurveyListFrag surveyListFrag);

    void inject(SurveyTrackingActivity surveyTrackingActivity);

    void inject(SurveyStatusActivity surveyStatusActivity);

    void inject(LogoutDialog logoutDialog);

    void inject(EditDialog editDialog);

    void inject(DeleteDialog deleteDialog);

    void inject(PropertyCreation propertyCreation);

    void inject(PropertySurvey propertySurvey);

    void inject(PhotoUpload photoUpload);

    void inject(PropertySurveyStatus propertySurveyStatus);

}