package com.instabug.cordova.plugin;

import android.content.Context;
import android.graphics.Color;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.instabug.library.invocation.util.InstabugFloatingButtonEdge;

public class MyApplication extends MultiDexApplication
{
    public static final String TAG = "MyApplication";

    @Override
    public void onCreate()
    {
        new Instabug.Builder(
                this,
                "ANDROID_APP_TOKEN",
                InstabugInvocationEvent.SHAKE
        ).setFloatingButtonEdge(InstabugFloatingButtonEdge.LEFT).setFloatingButtonOffsetFromTop(250).build();
        Instabug.setPrimaryColor(Color.parseColor("#1D82DC"));
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
