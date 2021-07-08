package com.thresholdsoft.wakfboard.data.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.thresholdsoft.wakfboard.root.AppConstant;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jagadeesh on 03/06/2020.
 */

public class TokenInterceptor implements Interceptor {
    private static final String AUTHORIZATION = "Authorization";
    private final Context context;


    public TokenInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = null;
        String authToken;
        SharedPreferences preferences = context.getSharedPreferences(AppConstant.PREF_NAME, MODE_PRIVATE);
        authToken = preferences.getString("PREF_KEY_ACCESS_TOKEN", "");
        Request.Builder requestBuilder = null;
        requestBuilder = original.newBuilder()
                .header(AUTHORIZATION, "bearer " + authToken)
                .method(original.method(), original.body());
        request = requestBuilder.build();
        Log.e("Token", "bearer " + authToken);
        return chain.proceed(request);
    }
}