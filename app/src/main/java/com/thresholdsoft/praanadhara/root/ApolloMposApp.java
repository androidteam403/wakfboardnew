package com.thresholdsoft.praanadhara.root;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.thresholdsoft.praanadhara.utils.CustomFontFamily;

public class ApolloMposApp extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    CustomFontFamily customFontFamily;

    @Override
    public void onCreate() {
        super.onCreate();
        ApolloMposApp.context = this;

        customFontFamily = CustomFontFamily.getInstance();
// add your custom fonts here with your own custom name.
        customFontFamily.addFont("robotoLight", "roboto_light.ttf");
        customFontFamily.addFont("robotoRegular", "roboto_regular.ttf");
        customFontFamily.addFont("robotoMedium", "roboto_medium.ttf");
        customFontFamily.addFont("robotoBold", "roboto_bold.ttf");
    }

    //main
    //ui or fragments , MainActivity
    //neworder
    //orderhistory
// Needed to replace the component with a test specific one
    public static Context getContext() {
        return context;
    }
}