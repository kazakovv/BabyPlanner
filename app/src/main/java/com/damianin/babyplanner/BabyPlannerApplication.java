package com.damianin.babyplanner;

import android.app.Application;

import com.backendless.Backendless;

/**
 * Created by Victor on 18/05/2015.
 */
public class BabyPlannerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        Backendless.setUrl("https://api.backendless.com");
        Backendless.initApp(this, Keys.appID, Keys.sectetKey, Keys.appVersion);
    }
}
