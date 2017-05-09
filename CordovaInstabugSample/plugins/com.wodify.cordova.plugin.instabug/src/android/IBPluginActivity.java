package com.wodify.cordova.plugin.instabug;

import java.lang.Integer;
import java.lang.Float;
import java.lang.Boolean;

import android.os.Bundle;
import org.apache.cordova.*;

import com.instabug.library.Instabug;
import com.instabug.library.Instabug.Builder;
import com.instabug.library.IBGInvocationEvent;
import com.instabug.library.IBGInvocationMode;
import com.instabug.library.IBGColorTheme;
import com.instabug.library.IBGFloatingButtonEdge;
import com.instabug.library.Feature;

public class IBPluginActivity extends CordovaActivity
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

        IBGInvocationEvent event = IBPlugin.parseInvocationEvent(options.getString("invocationEvent"));

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
        IBPluginActivity.this.finish();
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
     * desired default invocation mode.
     * @param mode
     *        String shortcode for IBGInvocationMode
     */
    private void setDefaultInvocationMode(String mode) {
        IBGInvocationMode iMode = IBPlugin.parseInvocationMode(mode);

        if (iMode != null) {
            builder.setDefaultInvocationMode(iMode);
        }
    }

    /**
     * Convenience method for parsing and setting the
     * shaking threshold.
     * @param threshold
     *        String representation of float threshold
     */
    private void setShakingThreshold(String threshold) {
        if (threshold != null && threshold.length() > 0) {
            builder.setShakingThreshold(Float.parseFloat(threshold));
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
            builder.setFloatingButtonEdge(IBGFloatingButtonEdge.Left);
        } else if ("right".equals(edge)) {
            builder.setFloatingButtonEdge(IBGFloatingButtonEdge.Right);
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
            builder.setDebugEnabled(Boolean.parseBoolean(enabled));
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
            builder.setShouldShowIntroDialog(Boolean.parseBoolean(enabled));
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
     *        String representation of IBGColorTheme
     */
    private void setColorTheme(String theme) {
        if ("dark".equals(theme)) {
            builder.setColorTheme(IBGColorTheme.IBGColorThemeDark);
        } else if ("light".equals(theme)) {
            builder.setColorTheme(IBGColorTheme.IBGColorThemeLight);
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
        setDefaultInvocationMode(opts.getString("defaultInvocationMode"));
        setShakingThreshold(opts.getString("shakingThresholdAndroid"));
        setFloatingButtonEdge(opts.getString("floatingButtonEdge"));
        setFloatingButtonOffset(opts.getString("floatingButtonOffset"));
        setDebugEnabled(opts.getString("enableDebug"));
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
