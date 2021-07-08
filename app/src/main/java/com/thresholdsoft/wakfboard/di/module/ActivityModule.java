package com.thresholdsoft.wakfboard.di.module;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.thresholdsoft.wakfboard.di.ActivityContext;
import com.thresholdsoft.wakfboard.di.PerActivity;
import com.thresholdsoft.wakfboard.ui.mainactivity.MainActivityMvpPresenter;
import com.thresholdsoft.wakfboard.ui.mainactivity.MainActivityMvpView;
import com.thresholdsoft.wakfboard.ui.mainactivity.MainActivityPresenter;
import com.thresholdsoft.wakfboard.ui.mainactivity.dialog.LogoutMvpPresenter;
import com.thresholdsoft.wakfboard.ui.mainactivity.dialog.LogoutMvpView;
import com.thresholdsoft.wakfboard.ui.mainactivity.dialog.LogoutPresenter;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.newenrollmentfrag.NewEnrollmentFragMvpPresenter;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.newenrollmentfrag.NewEnrollmentFragMvpView;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.newenrollmentfrag.NewEnrollmentFragPresenter;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.SurveyListMvpPresenter;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.SurveyListMvpView;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.SurveyListPresenter;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.userprofilefrag.UserProfileMvpPresenter;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.userprofilefrag.UserProfileMvpView;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.userprofilefrag.UserProfilePresenter;
import com.thresholdsoft.wakfboard.ui.mapdataliastactivity.MapDataListActivityMvpPresenter;
import com.thresholdsoft.wakfboard.ui.mapdataliastactivity.MapDataListActivityMvpView;
import com.thresholdsoft.wakfboard.ui.mapdataliastactivity.MapDataListActivityPresenter;
import com.thresholdsoft.wakfboard.ui.photouploadactivity.PhotoUploadMvpPresenter;
import com.thresholdsoft.wakfboard.ui.photouploadactivity.PhotoUploadMvpView;
import com.thresholdsoft.wakfboard.ui.photouploadactivity.PhotoUploadPresenter;
import com.thresholdsoft.wakfboard.ui.propertycreation.PropertyMvpPresenter;
import com.thresholdsoft.wakfboard.ui.propertycreation.PropertyMvpView;
import com.thresholdsoft.wakfboard.ui.propertycreation.PropertyPresenter;
import com.thresholdsoft.wakfboard.ui.propertysurvey.PropertySurveyMvpPresenter;
import com.thresholdsoft.wakfboard.ui.propertysurvey.PropertySurveyMvpView;
import com.thresholdsoft.wakfboard.ui.propertysurvey.PropertySurveyPresenter;
import com.thresholdsoft.wakfboard.ui.propertysurveystatus.PropertySurveyStatusMvpPresenter;
import com.thresholdsoft.wakfboard.ui.propertysurveystatus.PropertySurveyStatusMvpView;
import com.thresholdsoft.wakfboard.ui.propertysurveystatus.PropertySurveyStatusPresenter;
import com.thresholdsoft.wakfboard.ui.selectingformactivity.SelectingFormMvpPresenter;
import com.thresholdsoft.wakfboard.ui.selectingformactivity.SelectingFormMvpView;
import com.thresholdsoft.wakfboard.ui.selectingformactivity.SelectingFormPresenter;
import com.thresholdsoft.wakfboard.ui.splash.SplashMvpPresenter;
import com.thresholdsoft.wakfboard.ui.splash.SplashMvpView;
import com.thresholdsoft.wakfboard.ui.splash.SplashPresenter;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.SurveyStatusMvpPresenter;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.SurveyStatusMvpView;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.SurveyStatusPresenter;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.dialog.deletedialog.DeleteMvpPresenter;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.dialog.deletedialog.DeleteMvpView;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.dialog.deletedialog.DeletePresenter;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.dialog.editdialog.EditDialogMvpPresenter;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.dialog.editdialog.EditDialogMvpView;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.dialog.editdialog.EditDialogPresenter;
import com.thresholdsoft.wakfboard.ui.surveytrack.SurveyTrackMvpPresenter;
import com.thresholdsoft.wakfboard.ui.surveytrack.SurveyTrackMvpView;
import com.thresholdsoft.wakfboard.ui.surveytrack.SurveyTrackPresenter;
import com.thresholdsoft.wakfboard.ui.userlogin.UserLoginMvpPresenter;
import com.thresholdsoft.wakfboard.ui.userlogin.UserLoginMvpView;
import com.thresholdsoft.wakfboard.ui.userlogin.UserLoginPresenter;
import com.thresholdsoft.wakfboard.utils.rx.AppSchedulerProvider;
import com.thresholdsoft.wakfboard.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }


    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @PerActivity
    SplashMvpPresenter<SplashMvpView> provideSplashPresenter(SplashPresenter<SplashMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    UserLoginMvpPresenter<UserLoginMvpView> provideLoginPresenter(UserLoginPresenter<UserLoginMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SelectingFormMvpPresenter<SelectingFormMvpView> provideFormPresenter(SelectingFormPresenter<SelectingFormMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MainActivityMvpPresenter<MainActivityMvpView> mainActivityPresenter(MainActivityPresenter<MainActivityMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    UserProfileMvpPresenter<UserProfileMvpView> dashboardFragmentPresenter(UserProfilePresenter<UserProfileMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    NewEnrollmentFragMvpPresenter<NewEnrollmentFragMvpView> newEnrollmentFragmentPresenter(NewEnrollmentFragPresenter<NewEnrollmentFragMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SurveyListMvpPresenter<SurveyListMvpView> surveyListPresenter(SurveyListPresenter<SurveyListMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SurveyTrackMvpPresenter<SurveyTrackMvpView> surveyTrackPresenter(SurveyTrackPresenter<SurveyTrackMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SurveyStatusMvpPresenter<SurveyStatusMvpView> surveyStatusPresenter(SurveyStatusPresenter<SurveyStatusMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    LogoutMvpPresenter<LogoutMvpView> logoutMvpViewLogoutMvpPresenter(LogoutPresenter<LogoutMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    EditDialogMvpPresenter<EditDialogMvpView> EditDialogMvpPresenter(EditDialogPresenter<EditDialogMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    DeleteMvpPresenter<DeleteMvpView> deletePresenter(DeletePresenter<DeleteMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    PropertyMvpPresenter<PropertyMvpView> propertyMvpViewPropertyMvpPresenter(PropertyPresenter<PropertyMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    PropertySurveyMvpPresenter<PropertySurveyMvpView> propertySurveyMvpPresenter(PropertySurveyPresenter<PropertySurveyMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    PhotoUploadMvpPresenter<PhotoUploadMvpView> photoUploadMvpViewPhotoUploadMvpPresenter(PhotoUploadPresenter<PhotoUploadMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    PropertySurveyStatusMvpPresenter<PropertySurveyStatusMvpView> propertySurveyStatusMvpPresenter(PropertySurveyStatusPresenter<PropertySurveyStatusMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MapDataListActivityMvpPresenter<MapDataListActivityMvpView> mapDataListActivityMvpViewMainActivityMvpPresenter(MapDataListActivityPresenter<MapDataListActivityMvpView> presenter) {
        return presenter;
    }

}