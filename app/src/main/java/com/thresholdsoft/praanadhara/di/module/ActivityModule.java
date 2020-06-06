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
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.userprofile.UserProfileMvpPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.userprofile.UserProfileMvpView;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.userprofile.UserProfilePresenter;
import com.thresholdsoft.praanadhara.ui.selectingformactivity.SelectingFormMvpPresenter;
import com.thresholdsoft.praanadhara.ui.selectingformactivity.SelectingFormMvpView;
import com.thresholdsoft.praanadhara.ui.selectingformactivity.SelectingFormPresenter;
import com.thresholdsoft.praanadhara.ui.splash.SplashMvpPresenter;
import com.thresholdsoft.praanadhara.ui.splash.SplashMvpView;
import com.thresholdsoft.praanadhara.ui.splash.SplashPresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpView;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusPresenter;
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

}