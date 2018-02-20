package com.imagesoftware.anubhav.vacmet;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.github.ajalt.reprint.core.Reprint;

/**
 * Created by Anubhav-Singh on 20-02-2018.
 */

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Reprint.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
}
