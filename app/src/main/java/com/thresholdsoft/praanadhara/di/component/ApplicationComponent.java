package com.thresholdsoft.praanadhara.di.component;

import android.app.Application;
import android.content.Context;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.di.ApplicationContext;
import com.thresholdsoft.praanadhara.di.module.ApplicationModule;
import com.thresholdsoft.praanadhara.root.WaveApp;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(WaveApp app);

    @ApplicationContext
    Context context();

    Application application();

    DataManager getDataManager();
//    @Component.Builder
//    interface Builder {
//        ApplicationComponent build();
//
//        Builder applicationModule(ApplicationModule applicationModule);
//
//        DataManager getDataManager();
//    }


}
