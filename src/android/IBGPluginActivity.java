package com.instabug.cordova.plugin;

import android.os.Bundle;

import com.instabug.library.Feature;
import com.instabug.library.Instabug;
import com.instabug.library.InstabugColorTheme;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.instabug.library.invocation.InstabugInvocationMode;
import com.instabug.library.invocation.util.InstabugFloatingButtonEdge;

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
        
        InstabugInvocationEvent event = IBGPlugin.parseInvocationEvent(options.getString("invocationEvent"));

        if (event != null) {
            // Initialize builder with invocation event if possible
            builder = new Instabug.Builder(
                this.getApplication(),
                options.getString("token"),
                event
            );
        } else {
            builder = new Instabug.Builder(
                this.getApplication(),
                options.getString("token")
            );
        }

        // Apply initialization options through builder
        setBuilderOptions(options);

        //builder.setShouldShowIntroDialog(false);
        // Initialize Instabug through builder
        builder.build();

        // Finish activity (required for no-display theme)
        IBGPluginActivity.this.finish();
    }

    /**
     * Convenience method for parsing and setting
     * whether the email field is required.
     *
     * @param required
     *        String representation of boolean required
     */
    private void setEmailFieldRequired(String required) {
        if (required != null && required.length() > 0) {
            builder.setEmailFieldRequired(Boolean.parseBoolean(required));
        }
    }

    /**
     * Convenience method for parsing and setting
     * whether the comment field is required.
     *
     * @param required
     *        String representation of boolean required
     */
    private void setCommentFieldRequired(String required) {
        if (required != null && required.length() > 0) {
            builder.setCommentFieldRequired(Boolean.parseBoolean(required));
        }
    }

    /**
     * Convenience method for parsing and setting the
     * shaking threshold.
     * @param threshold
     *        String representation of int threshold
     */
    private void setShakingThreshold(String threshold) {
        if (threshold != null && threshold.length() > 0) {
            builder.setShakingThreshold(Integer.parseInt(threshold));
        }
    }

    /**
     * Convenience method for parsing and setting the
     * edge on which to float the button.
     * @param edge
     *        String representation of edge
     */
    private void setFloatingButtonEdge(String edge) {
        if ("left".equals(edge)) {
            builder.setFloatingButtonEdge(InstabugFloatingButtonEdge.LEFT);
        } else if ("right".equals(edge)) {
            builder.setFloatingButtonEdge(InstabugFloatingButtonEdge.RIGHT);
        }
    }

    /**
     * Convenience method for parsing and setting the
     * offset from the top for the floating button.
     * @param offset
     *        String representation of int offset
     */
    private void setFloatingButtonOffset(String offset) {
        if (offset != null && offset.length() > 0) {
            builder.setFloatingButtonOffsetFromTop(Integer.parseInt(offset));
        }
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
        if ("true".equals(enabled)) {
            builder.setConsoleLogState(Feature.State.ENABLED);
        } else if ("false".equals(enabled)) {
            builder.setConsoleLogState(Feature.State.DISABLED);
        }
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
        if ("true".equals(enabled)) {
            builder.setInstabugLogState(Feature.State.ENABLED);
        } else if ("false".equals(enabled)) {
            builder.setInstabugLogState(Feature.State.DISABLED);
        }
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
        if ("true".equals(enabled)) {
            builder.setTrackingUserStepsState(Feature.State.ENABLED);
        } else if ("false".equals(enabled)) {
            builder.setTrackingUserStepsState(Feature.State.DISABLED);
        }
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
        if ("true".equals(enabled)) {
            builder.setCrashReportingState(Feature.State.ENABLED);
        } else if ("false".equals(enabled)) {
            builder.setCrashReportingState(Feature.State.DISABLED);
        }
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
        if ("true".equals(enabled)) {
            builder.setInAppMessagingState(Feature.State.ENABLED);
        } else if ("false".equals(enabled)) {
            builder.setInAppMessagingState(Feature.State.DISABLED);
        }
    }

    /**
     * Convenience method for parsing and setting
     * whether conversion sounds should be enabled
     * by the SDK.
     *
     * @param enabled
     *        String representation of boolean enabled
     */
    private void setConversationSoundsEnabled(String enabled) {
        if (enabled != null && enabled.length() > 0) {
            builder.setShouldPlayConversationSounds(Boolean.parseBoolean(enabled));
        }
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
        if ("true".equals(enabled)) {
            builder.setPushNotificationState(Feature.State.ENABLED);
        } else if ("false".equals(enabled)) {
            builder.setPushNotificationState(Feature.State.DISABLED);
        }
    }

     /**
     * Convenience method for parsing and setting
     * whether the intro dialog should be shown the
     * first time the app is opened.
     *
     * @param enabled
     *        String representation of boolean enabled
     */
    private void setIntroDialogEnabled(String enabled) {
        if (enabled != null && enabled.length() > 0) {
            builder.setIntroMessageEnabled(Boolean.parseBoolean(enabled));
        }
    }

    /**
     * Convenience method for parsing and setting
     * whether user data should be added to reports.
     *
     * @param enabled
     *        String representation of boolean enabled
     */
    private void setUserDataEnabled(String enabled) {
        if ("true".equals(enabled)) {
            builder.setUserDataState(Feature.State.ENABLED);
        } else if ("false".equals(enabled)) {
            builder.setUserDataState(Feature.State.DISABLED);
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
            builder.setTheme(InstabugColorTheme.InstabugColorThemeDark);
        } else if ("light".equals(theme)) {
            builder.setTheme(InstabugColorTheme.InstabugColorThemeLight);
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
        setEmailFieldRequired(opts.getString("emailRequired"));
        setCommentFieldRequired(opts.getString("commentRequired"));
        setShakingThreshold(opts.getString("shakingThresholdAndroid"));
        setFloatingButtonEdge(opts.getString("floatingButtonEdge"));
        setFloatingButtonOffset(opts.getString("floatingButtonOffset"));
        setConsoleLogsEnabled(opts.getString("enableConsoleLogs"));
        setInstabugLogsEnabled(opts.getString("enableInstabugLogs"));
        setTrackingUserStepsEnabled(opts.getString("enableTrackingUserSteps"));
        setCrashReportingEnabled(opts.getString("enableCrashReporting"));
        setInAppMessagingEnabled(opts.getString("enableInAppMessaging"));
        setConversationSoundsEnabled(opts.getString("enableConversationSounds"));
        setPushNotificationsEnabled(opts.getString("enablePushNotifications"));
        setIntroDialogEnabled(opts.getString("enableIntroDialog"));
        setUserDataEnabled(opts.getString("enableUserData"));
        setColorTheme(opts.getString("colorTheme"));
    }
}
