package com.thresholdsoft.praanadhara.root;

import android.app.Application;
import android.content.Context;

import com.thresholdsoft.praanadhara.di.component.ApplicationComponent;
import com.thresholdsoft.praanadhara.di.component.DaggerApplicationComponent;
import com.thresholdsoft.praanadhara.di.module.ApplicationModule;
import com.thresholdsoft.praanadhara.utils.CustomFontFamily;

/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
public class WaveApp extends Application {

    private ApplicationComponent mApplicationComponent;
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        WaveApp.context = this;
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();
        mApplicationComponent.inject(this);
        CustomFontFamily customFontFamily = CustomFontFamily.getInstance();
        // add your custom fonts here with your own custom name.
        customFontFamily.addFont("robotoLight", "roboto_light.ttf");
        customFontFamily.addFont("robotoRegular", "roboto_regular.ttf");
        customFontFamily.addFont("robotoMedium", "roboto_medium.ttf");
        customFontFamily.addFont("robotoBold", "roboto_bold.ttf");
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }


    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    public static Context getContext() {
        return context;
    }
}
