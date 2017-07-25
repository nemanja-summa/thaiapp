package io.summa.tutorial;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by nemanja on 7/25/17.
 */

public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        Fabric.with(this, new Crashlytics());

    }
    public static Context getAppContext() {
        return mContext;
    }

}
