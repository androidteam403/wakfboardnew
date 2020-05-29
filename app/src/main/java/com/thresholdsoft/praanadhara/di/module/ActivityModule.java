package com.thresholdsoft.praanadhara.di.module;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.thresholdsoft.praanadhara.di.ActivityContext;
import com.thresholdsoft.praanadhara.di.PerActivity;
import com.thresholdsoft.praanadhara.ui.mainactivity.MainActivityMvpPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.MainActivityMvpView;
import com.thresholdsoft.praanadhara.ui.mainactivity.MainActivityPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.dasboardfrag.DashboardMvpPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.dasboardfrag.DashboardMvpView;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.dasboardfrag.DashboardPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.newenrollmentfrag.NewEnrollmentFragMvpPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.newenrollmentfrag.NewEnrollmentFragMvpView;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.newenrollmentfrag.NewEnrollmentFragPresenter;
import com.thresholdsoft.praanadhara.ui.selectingformactivity.SelectingFormMvpPresenter;
import com.thresholdsoft.praanadhara.ui.selectingformactivity.SelectingFormMvpView;
import com.thresholdsoft.praanadhara.ui.selectingformactivity.SelectingFormPresenter;
import com.thresholdsoft.praanadhara.ui.splash.SplashMvpPresenter;
import com.thresholdsoft.praanadhara.ui.splash.SplashMvpView;
import com.thresholdsoft.praanadhara.ui.splash.SplashPresenter;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.SurveyListMvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.SurveyListMvpView;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.SurveyListPresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpView;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusPresenter;
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
    DashboardMvpPresenter<DashboardMvpView> dashboardFragmentPresenter(DashboardPresenter<DashboardMvpView> presenter) {
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
    SurveyStatusMvpPresenter<SurveyStatusMvpView> surveyStatusPresenter(SurveyStatusPresenter<SurveyStatusMvpView> presenter) {
        return presenter;
    }
}