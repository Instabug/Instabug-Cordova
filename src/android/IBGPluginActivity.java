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
     * Convenience method for parsing and setting the
     * edge on which to float the button.
     * @param edge
     *        String representation of edge
     */
    private void setFloatingButtonEdge(final String edge) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (edge != null && edge.equals("left"))
                    BugReporting.setFloatingButtonEdge(InstabugFloatingButtonEdge.LEFT);
                else
                    BugReporting.setFloatingButtonEdge(InstabugFloatingButtonEdge.RIGHT);
            }
        });
    }

    /**
     * Convenience method for parsing and setting the
     * offset from the top for the floating button.
     * @param offset
     *        String representation of int offset
     */
    private void setFloatingButtonOffset(final String offset) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (offset != null)
                    BugReporting.setFloatingButtonOffset(Integer.parseInt(offset));
            }
        });
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
     * whether tracking user steps should be enabled
     * by the SDK.
     *
     * @param enabled
     *        String representation of boolean enabled
     */
    private void setTrackingUserStepsEnabled(String enabled) {

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
     * whether push notifications should be enabled
     * by the SDK.
     *
     * @param enabled
     *        String representation of boolean enabled
     */
    private void setPushNotificationsEnabled(String enabled) {

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
     * Convenience method for parsing and setting
     * whether session profiler should be enabled or not.
     *
     * @param enabled
     *        String representation of boolean enabled
     */
    private void setSessionProfilerEnabled(String enabled) {
      if (enabled != null && enabled.length() > 0) {
        if(Boolean.parseBoolean(enabled)) {
          Instabug.setSessionProfilerState(Feature.State.ENABLED);
        } else {
          Instabug.setSessionProfilerState(Feature.State.DISABLED);
        }
      }
    }

    /**
     * Convenience method for parsing and setting the welcome message mode
     *
     * @param welcomeMessageMode
     *        String representation of welcomeMessageMode
     */
    private void setWelcomeMessageMode(String welcomeMessageMode) {
        if (welcomeMessageMode != null && welcomeMessageMode.length() > 0) {
            if (welcomeMessageMode.equals("welcomeMessageModeLive")) {
                Instabug.setWelcomeMessageState(WelcomeMessage.State.LIVE);
            } else if (welcomeMessageMode.equals("welcomeMessageModeBeta")) {
                Instabug.setWelcomeMessageState(WelcomeMessage.State.BETA);
            } else if (welcomeMessageMode.equals("welcomeMessageModeDisabled")) {
                Instabug.setWelcomeMessageState(WelcomeMessage.State.DISABLED);
            } else {
                Instabug.setWelcomeMessageState(WelcomeMessage.State.LIVE);
            }
        }
      }

    /**
     * Convenience method for parsing and setting
     * whether the desired color theme for the SDK
     * invocation.
     *
     * @param theme
     *        String representation of InstabugColorTheme
     */
    private void setColorTheme(String theme) {
        if ("dark".equals(theme)) {
            Instabug.setColorTheme(InstabugColorTheme.InstabugColorThemeDark);
        } else if ("light".equals(theme)) {
            Instabug.setColorTheme(InstabugColorTheme.InstabugColorThemeLight);
        }
    }

    /**
     * Wrapper method for parsing and setting all
     * provided options.
     *
     * @param opts
     *        JSONObject representation of options
     */
    private void setBuilderOptions(Bundle opts) {
        setFloatingButtonEdge(opts.getString("floatingButtonEdge"));
        setFloatingButtonOffset(opts.getString("floatingButtonOffset"));
        setConsoleLogsEnabled(opts.getString("enableConsoleLogs"));
        setInstabugLogsEnabled(opts.getString("enableInstabugLogs"));
        setTrackingUserStepsEnabled(opts.getString("enableTrackingUserSteps"));
        setCrashReportingEnabled(opts.getString("enableCrashReporting"));
        setInAppMessagingEnabled(opts.getString("enableInAppMessaging"));
        setPushNotificationsEnabled(opts.getString("enablePushNotifications"));
        setUserDataEnabled(opts.getString("enableUserData"));
        setColorTheme(opts.getString("colorTheme"));
        setSessionProfilerEnabled(opts.getString("enableSessionProfiler"));
        setWelcomeMessageMode(opts.getString("welcomeMessageMode"));
    }
}
