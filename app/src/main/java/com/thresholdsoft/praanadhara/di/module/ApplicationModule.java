package com.thresholdsoft.praanadhara.di.module;

import android.app.Application;
import android.content.Context;

import com.thresholdsoft.praanadhara.BuildConfig;
import com.thresholdsoft.praanadhara.data.BaseDataManager;
import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.data.db.AppDatabase;
import com.thresholdsoft.praanadhara.data.network.NetworkService;
import com.thresholdsoft.praanadhara.data.network.RestApiHelper;
import com.thresholdsoft.praanadhara.data.network.RestApiManager;
import com.thresholdsoft.praanadhara.data.network.TokenInterceptor;
import com.thresholdsoft.praanadhara.data.prefs.PreferencesHelper;
import com.thresholdsoft.praanadhara.data.prefs.PreferencesManager;
import com.thresholdsoft.praanadhara.di.ApiInfo;
import com.thresholdsoft.praanadhara.di.ApplicationContext;
import com.thresholdsoft.praanadhara.di.DatabaseInfo;
import com.thresholdsoft.praanadhara.di.PreferenceInfo;
import com.thresholdsoft.praanadhara.root.AppConstant;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
@Module
public class ApplicationModule {

    private Application mApplication;

    public ApplicationModule(Application application) {
        this.mApplication = application;
    }


    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstant.DB_NAME;
    }

    @Provides
    @ApiInfo
    String provideApiKey() {
        return BuildConfig.API_KEY;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstant.PREF_NAME;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(BaseDataManager mDataManager) {
        return mDataManager;
    }


    @Provides
    @Singleton
    AppDatabase provideAppDatabase() {
        return AppDatabase.getDatabaseInstance(mApplication);
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(PreferencesManager preferencesManager) {
        return preferencesManager;
    }

    @Provides
    @Singleton
    RestApiHelper provideRestApiHelper(RestApiManager restApiManager) {
        return restApiManager;
    }


    /**
     * @return HTTTP Client
     */
    @Provides
    public OkHttpClient provideClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(new TokenInterceptor(mApplication)).addInterceptor(chain -> {
            Request request = chain.request();
            return chain.proceed(request);
        }).build();
    }

    /**
     * provide Retrofit instances
     *
     * @param baseURL base url for api calling
     * @param client  OkHttp client
     * @return Retrofit instances
     */

    @Provides
    public Retrofit provideRetrofit(String baseURL, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Provide Api service
     *
     * @return ApiService instances
     */
    @Provides
    public NetworkService provideApiService() {
        return provideRetrofit(BuildConfig.BASE_URL, provideClient()).create(NetworkService.class);
    }
}
