package com.thresholdsoft.praanadhara.di.component;


import com.thresholdsoft.praanadhara.di.PerActivity;
import com.thresholdsoft.praanadhara.di.module.ActivityModule;
import com.thresholdsoft.praanadhara.ui.login.LoginActivity;
import com.thresholdsoft.praanadhara.ui.main.MainActivity;

import dagger.Component;

/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {


    void inject(LoginActivity loginActivity);

    void inject(MainActivity mainActivity);
}