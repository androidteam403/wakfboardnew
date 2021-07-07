package com.thresholdsoft.praanadhara.di.module;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.thresholdsoft.praanadhara.di.ActivityContext;
import com.thresholdsoft.praanadhara.di.PerActivity;
import com.thresholdsoft.praanadhara.ui.mainactivity.MainActivityMvpPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.MainActivityMvpView;
import com.thresholdsoft.praanadhara.ui.mainactivity.MainActivityPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.dialog.LogoutMvpPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.dialog.LogoutMvpView;
import com.thresholdsoft.praanadhara.ui.mainactivity.dialog.LogoutPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.newenrollmentfrag.NewEnrollmentFragMvpPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.newenrollmentfrag.NewEnrollmentFragMvpView;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.newenrollmentfrag.NewEnrollmentFragPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.SurveyListMvpPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.SurveyListMvpView;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.SurveyListPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.userprofilefrag.UserProfileMvpPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.userprofilefrag.UserProfileMvpView;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.userprofilefrag.UserProfilePresenter;
import com.thresholdsoft.praanadhara.ui.mapdataliastactivity.MapDataListActivityMvpPresenter;
import com.thresholdsoft.praanadhara.ui.mapdataliastactivity.MapDataListActivityMvpView;
import com.thresholdsoft.praanadhara.ui.mapdataliastactivity.MapDataListActivityPresenter;
import com.thresholdsoft.praanadhara.ui.photouploadactivity.PhotoUploadMvpPresenter;
import com.thresholdsoft.praanadhara.ui.photouploadactivity.PhotoUploadMvpView;
import com.thresholdsoft.praanadhara.ui.photouploadactivity.PhotoUploadPresenter;
import com.thresholdsoft.praanadhara.ui.propertycreation.PropertyMvpPresenter;
import com.thresholdsoft.praanadhara.ui.propertycreation.PropertyMvpView;
import com.thresholdsoft.praanadhara.ui.propertycreation.PropertyPresenter;
import com.thresholdsoft.praanadhara.ui.propertysurvey.PropertySurveyMvpPresenter;
import com.thresholdsoft.praanadhara.ui.propertysurvey.PropertySurveyMvpView;
import com.thresholdsoft.praanadhara.ui.propertysurvey.PropertySurveyPresenter;
import com.thresholdsoft.praanadhara.ui.propertysurveystatus.PropertySurveyStatusMvpPresenter;
import com.thresholdsoft.praanadhara.ui.propertysurveystatus.PropertySurveyStatusMvpView;
import com.thresholdsoft.praanadhara.ui.propertysurveystatus.PropertySurveyStatusPresenter;
import com.thresholdsoft.praanadhara.ui.selectingformactivity.SelectingFormMvpPresenter;
import com.thresholdsoft.praanadhara.ui.selectingformactivity.SelectingFormMvpView;
import com.thresholdsoft.praanadhara.ui.selectingformactivity.SelectingFormPresenter;
import com.thresholdsoft.praanadhara.ui.splash.SplashMvpPresenter;
import com.thresholdsoft.praanadhara.ui.splash.SplashMvpView;
import com.thresholdsoft.praanadhara.ui.splash.SplashPresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpView;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusPresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.deletedialog.DeleteMvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.deletedialog.DeleteMvpView;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.deletedialog.DeletePresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.editdialog.EditDialogMvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.editdialog.EditDialogMvpView;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.editdialog.EditDialogPresenter;
import com.thresholdsoft.praanadhara.ui.surveytrack.SurveyTrackMvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveytrack.SurveyTrackMvpView;
import com.thresholdsoft.praanadhara.ui.surveytrack.SurveyTrackPresenter;
import com.thresholdsoft.praanadhara.ui.userlogin.UserLoginMvpPresenter;
import com.thresholdsoft.praanadhara.ui.userlogin.UserLoginMvpView;
import com.thresholdsoft.praanadhara.ui.userlogin.UserLoginPresenter;
import com.thresholdsoft.praanadhara.utils.rx.AppSchedulerProvider;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

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