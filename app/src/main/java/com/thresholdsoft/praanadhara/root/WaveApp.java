package com.thresholdsoft.praanadhara.root;

import android.app.Application;

import com.thresholdsoft.praanadhara.di.component.ApplicationComponent;
import com.thresholdsoft.praanadhara.di.component.DaggerApplicationComponent;
import com.thresholdsoft.praanadhara.di.module.ApplicationModule;

/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
public class WaveApp extends Application {


    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();
        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }


    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
