package com.instabug.cordova.plugin;

import android.app.Application;
import android.graphics.Color;
import android.util.Log;

import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.instabug.library.invocation.util.InstabugFloatingButtonEdge;

public class MyApplication extends Application
{
    public static final String TAG = "MyApplication";

    @Override
    public void onCreate()
    {
        new Instabug.Builder(
                this,
                "ANDROID_APP_TOKEN",
                InstabugInvocationEvent.FLOATING_BUTTON
        ).setFloatingButtonEdge(InstabugFloatingButtonEdge.LEFT).setFloatingButtonOffsetFromTop(250).build();
        Instabug.setPrimaryColor(Color.parseColor("#1D82DC"));
        super.onCreate();
    }
}
