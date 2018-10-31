package com.ty.nanfangscanner;

import android.app.Application;
import android.content.Context;

import com.pgyersdk.crash.PgyCrashManager;

public class App extends Application {
    private static App context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        PgyCrashManager.register(this);
    }

    public static Context getApplication() {
        return context;
    }
}
