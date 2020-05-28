package com.thresholdsoft.praanadhara.di.module;

import android.content.Context;

import com.thresholdsoft.praanadhara.di.ActivityContext;
import com.thresholdsoft.praanadhara.di.PerActivity;
import com.thresholdsoft.praanadhara.ui.login.LoginMvpPresenter;
import com.thresholdsoft.praanadhara.ui.login.LoginMvpView;
import com.thresholdsoft.praanadhara.ui.login.LoginPresenter;
import com.thresholdsoft.praanadhara.ui.main.MainMvpPresenter;
import com.thresholdsoft.praanadhara.ui.main.MainMvpView;
import com.thresholdsoft.praanadhara.ui.main.MainPresenter;
import com.thresholdsoft.praanadhara.ui.main.RssAdapter;
import com.thresholdsoft.praanadhara.utils.rx.AppSchedulerProvider;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
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
    LoginMvpPresenter<LoginMvpView> provideLoginPresenter(LoginPresenter<LoginMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    MainMvpPresenter<MainMvpView> provideMainPresenter(MainPresenter<MainMvpView> presenter) {
        return presenter;
    }


    @Provides
    RssAdapter provideRssAdapter() {
        return new RssAdapter(new ArrayList<>());
    }
}