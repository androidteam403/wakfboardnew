package com.thresholdsoft.wakfboard.di.component;

import android.app.Application;
import android.content.Context;

import com.thresholdsoft.wakfboard.data.DataManager;
import com.thresholdsoft.wakfboard.di.ApplicationContext;
import com.thresholdsoft.wakfboard.di.module.ApplicationModule;
import com.thresholdsoft.wakfboard.root.WaveApp;

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
