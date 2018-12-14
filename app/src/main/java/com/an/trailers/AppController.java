package com.an.trailers;

import android.app.Application;

import com.an.trailers.di.component.ApiComponent;
import com.an.trailers.di.component.DaggerApiComponent;
import com.an.trailers.di.module.ApiModule;
import com.an.trailers.di.module.AppModule;
import com.an.trailers.di.module.DbModule;

import static com.an.trailers.AppConstants.BASE_URL;


public class AppController extends Application {

    private ApiComponent apiComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        apiComponent = DaggerApiComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule(BASE_URL))
                .dbModule(new DbModule())
                .build();
    }

    public ApiComponent getApiComponent() {
        return apiComponent;
    }
}
