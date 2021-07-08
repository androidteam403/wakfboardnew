package com.thresholdsoft.wakfboard.di.component;


import com.thresholdsoft.wakfboard.di.PerActivity;
import com.thresholdsoft.wakfboard.di.module.ActivityModule;
import com.thresholdsoft.wakfboard.ui.mainactivity.MainActiivty;
import com.thresholdsoft.wakfboard.ui.mainactivity.dialog.LogoutDialog;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.newenrollmentfrag.NewEnrollmentFrag;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.SurveyListFrag;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.userprofilefrag.UserProfileFragment;
import com.thresholdsoft.wakfboard.ui.mapdataliastactivity.MapDataListActivity;
import com.thresholdsoft.wakfboard.ui.photouploadactivity.PhotoUpload;
import com.thresholdsoft.wakfboard.ui.propertycreation.PropertyCreation;
import com.thresholdsoft.wakfboard.ui.propertysurvey.PropertySurvey;
import com.thresholdsoft.wakfboard.ui.propertysurveystatus.PropertyPreview;
import com.thresholdsoft.wakfboard.ui.selectingformactivity.SelectingFormActivity;
import com.thresholdsoft.wakfboard.ui.splash.SplashActivity;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.SurveyStatusActivity;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.dialog.deletedialog.DeleteDialog;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.dialog.editdialog.EditDialog;
import com.thresholdsoft.wakfboard.ui.surveytrack.SurveyTrackingActivity;
import com.thresholdsoft.wakfboard.ui.userlogin.UserLoginActivity;

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

    void inject(PropertyPreview propertyPreview);

    void inject(MapDataListActivity mapDataListActivity);


}