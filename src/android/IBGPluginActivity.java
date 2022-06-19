package com.instabug.cordova.plugin;

import android.os.Bundle;
import android.util.Log;
import android.os.Handler;
import android.os.Looper;

import com.instabug.bug.BugReporting;
import com.instabug.library.Feature;
import com.instabug.library.Instabug;
import com.instabug.library.InstabugColorTheme;
import com.instabug.library.invocation.util.InstabugFloatingButtonEdge;
import com.instabug.library.ui.onboarding.WelcomeMessage;

import org.apache.cordova.CordovaActivity;


public class IBGPluginActivity extends CordovaActivity
{
    // Reference to builder
    private Instabug.Builder builder;

    /**
     * Called when activity starts. Initializes Instabug.
     *
     * @param savedInstanceState
     *        Saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve initialization options
        Bundle options = getIntent().getExtras();

        // Apply initialization options
        if(options != null) {
            setBuilderOptions(options);
        }
        // Finish activity (required for no-display theme)
        IBGPluginActivity.this.finish();
    }

    /**
     * Convenience method for parsing and setting
     * whether the debug logs from the Instabug SDK
     * are should be printed to LogCat.
     *
     * @param enabled
     *        String representation of boolean enabled
     */
    private void setDebugEnabled(String enabled) {
        if (enabled != null && enabled.length() > 0) {
            Instabug.setDebugEnabled(Boolean.parseBoolean(enabled));
        }
    }

    /**
     * Convenience method for parsing and setting
     * whether the console logs should be added to
     * reports.
     *
     * @param enabled
     *        String representation of boolean enabled
     */
    private void setConsoleLogsEnabled(String enabled) {

    }

    /**
     * Convenience method for parsing and setting
     * whether the debug logs from the Instabug SDK
     * should be added to reports.
     *
     * @param enabled
     *        String representation of boolean enabled
     */
    private void setInstabugLogsEnabled(String enabled) {

    }

    /**
     * Convenience method for parsing and setting
     * whether crash reporting should be enabled
     * by the SDK.
     *
     * @param enabled
     *        String representation of boolean enabled
     */
    private void setCrashReportingEnabled(String enabled) {

    }

    /**
     * Convenience method for parsing and setting
     * whether in-app messaging should be enabled
     * by the SDK.
     *
     * @param enabled
     *        String representation of boolean enabled
     */
    private void setInAppMessagingEnabled(String enabled) {

    }

    /**
     * Convenience method for parsing and setting
     * whether user data should be added to reports.
     *
     * @param enabled
     *        String representation of boolean enabled
     */
    private void setUserDataEnabled(String enabled) {

    }

    /**
     * Wrapper method for parsing and setting all
     * provided options.
     *
     * @param opts
     *        JSONObject representation of options
     */
    private void setBuilderOptions(Bundle opts) {
        setConsoleLogsEnabled(opts.getString("enableConsoleLogs"));
        setInstabugLogsEnabled(opts.getString("enableInstabugLogs"));
        setCrashReportingEnabled(opts.getString("enableCrashReporting"));
        setInAppMessagingEnabled(opts.getString("enableInAppMessaging"));
        setUserDataEnabled(opts.getString("enableUserData"));
    }
}
