package com.instabug.cordova.plugin;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import com.instabug.library.Feature;
import com.instabug.library.Instabug;
import com.instabug.library.extendedbugreport.ExtendedBugReport;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.instabug.library.invocation.InstabugInvocationMode;
import com.instabug.library.invocation.util.InstabugVideoRecordingButtonCorner;
import com.instabug.library.visualusersteps.State;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult.Status;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.Integer;
import java.util.HashMap;

/**
 * This plugin initializes Instabug.
 */
public class IBGPlugin extends CordovaPlugin {

    // Reference to intent that start activity
    // to initialize Instabug
    private Intent activationIntent;

    // Initialization options
    private JSONObject options;

    // All possible option keys
    private final String[] optionKeys = {
        "emailRequired",
        "commentRequired",
        "shakingThresholdAndroid",
        "floatingButtonEdge",
        "floatingButtonOffset",
        "enableDebug",
        "enableConsoleLogs",
        "enableInstabugLogs",
        "enableTrackingUserSteps",
        "enableCrashReporting",
        "enableInAppMessaging",
        "enableConversationSounds",
        "enablePushNotifications",
        "enableIntroDialog",
        "enableUserData",
        "colorTheme",
        "enableSessionProfiler"
    };

    // Generic error message
    private final String errorMsg = "Instabug object must first be activated.";

    /**
     * Called after plugin construction and fields have been initialized.
     */
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        // Initialize intent so that extras can be attached subsequently
        activationIntent = new Intent(cordova.getActivity(), com.instabug.cordova.plugin.IBGPluginActivity.class);

        options = new JSONObject();
    }

    /**
     * Executes the request sent from JavaScript.
     *
     * @param action
     *      The action to execute.
     * @param args
     *      The exec() arguments in JSON form.
     * @param callbackContext
     *      The callback context used when calling back into JavaScript.
     * @return
     *      Whether the action was valid.
     */
    @Override
    public boolean execute(final String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

        if ("activate".equals(action)) {
            activate(callbackContext, args);

        } else if ("invoke".equals(action)) {
            invoke(callbackContext, args.optString(0));

        } else if ("showIntroDialog".equals(action)) {
            showIntroDialog(callbackContext);

        } else if ("setPrimaryColor".equals(action)) {
            setPrimaryColor(callbackContext, args.optString(0));

        } else if ("setUserEmail".equals(action)) {
            setUserEmail(callbackContext, args.optString(0));

        } else if ("setUserName".equals(action)) {
            setUserName(callbackContext, args.optString(0));

        } else if ("setUserData".equals(action)) {
            setUserData(callbackContext, args.optString(0));

        } else if ("addFile".equals(action)) {
            addFile(callbackContext, args);

        } else if ("addLog".equals(action)) {
            addLog(callbackContext, args.optString(0));

        } else if ("clearLog".equals(action)) {
            clearLog(callbackContext);

        } else if ("changeInvocationEvent".equals(action)) {
            changeInvocationEvent(callbackContext, args.optString(0));

        } else if ("setInvocationEvents".equals(action)) {
            setInvocationEvent(callbackContext, args.optString(0));
            
        } else if ("disable".equals(action)) {
            disable(callbackContext);

        } else if ("enable".equals(action)) {
            enable(callbackContext);

        } else if ("isEnabled".equals(action)) {
            getIsEnabled(callbackContext);

        } else if ("setUserAttribute".equals(action)) {
            setUserAttribute(callbackContext, args.optString(0), args.optString(1));

        } else if ("setDebugEnabled".equals(action)) {
            setDebugEnabled(callbackContext, args.optBoolean(0));

        } else if ("removeUserAttribute".equals(action)) {
            removeUserAttribute(callbackContext, args.optString(0));

        }  else if ("identifyUserWithEmail".equals(action)) {
            identifyUserWithEmail(callbackContext, args.optString(0), args.optString(1));

        }  else if ("logOut".equals(action)) {
            logOut(callbackContext);

        }  else if ("getAllUserAttributes".equals(action)) {
            getAllUserAttributes(callbackContext);

        } else if ("getUserAttribute".equals(action)) {
            getUserAttribute(callbackContext, args.optString(0));

        } else if ("showSurveyWithToken".equals(action)) {
            showSurveyWithToken(callbackContext, args.optString(0));

        } else if ("hasRespondedToSurveyWithToken".equals(action)) {
            hasRespondedToSurveyWithToken(callbackContext, args.optString(0));

        } else if ("setVideoRecordingFloatingButtonPosition".equals(action)) {
            setVideoRecordingFloatingButtonPosition(callbackContext, args.optString(0));

        } else if ("setViewHierarchyEnabled".equals(action)) {
            setViewHierarchyEnabled(callbackContext, args.optBoolean(0));

        } else if ("setAutoScreenRecordingEnabled".equals(action)) {
            setAutoScreenRecordingEnabled(callbackContext, args.optBoolean(0));

        } else if ("setAutoScreenRecordingMaxDuration".equals(action)) {
            setAutoScreenRecordingMaxDuration(callbackContext, args.optInt(0));

        } else if ("setExtendedBugReportMode".equals(action)) {
            setExtendedBugReportMode(callbackContext, args.optString(0));

        } else if ("setReproStepsMode".equals(action)) {
            setReproStepsMode(callbackContext, args.optString(0));

        } else if ("setThresholdForReshowingSurveyAfterDismiss".equals(action)) {
            setThresholdForReshowingSurveyAfterDismiss(callbackContext, args.optInt(0), args.optInt(1));

        } else if ("showFeatureRequests".equals(action)) {
            showFeatureRequests(callbackContext);

        } else if ("setShouldShowSurveysWelcomeScreen".equals(action)) {
            setShouldShowSurveysWelcomeScreen(callbackContext, args.optBoolean(0));

        } else {
            // Method not found.
            return false;
        }

        return true;
    }

    /**
     * Creates intent to initialize Instabug.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    private void activate(final CallbackContext callbackContext, JSONArray args) {
        this.options = args.optJSONObject(2);
        if (options != null) {
            // Attach extras
            applyOptions();
        }
        cordova.getActivity().startActivity(activationIntent);

        callbackContext.success();


    }

    /**
     * Shows the Instabug dialog so user can choose to report a bug, or
     * submit feedback. A specific mode of the SDK can be shown if specified.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param mode
     *        Specific mode of SDK
     */
    private void invoke(final CallbackContext callbackContext, String mode) {
        InstabugInvocationMode iMode = parseInvocationMode(mode);

        try {
            //Instabug instabug = Instabug.getInstance();

            if (iMode != null) {
                // Invoke specific mode if possible
                Instabug.invoke(iMode);
            } else {
                Instabug.invoke();
            }
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Display the discovery dialog explaining the shake gesture or the
     * two finger swipe gesture, if you've enabled it.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    private void showIntroDialog(final CallbackContext callbackContext) {
        try {
            Instabug.showIntroMessage();
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Set the primary color that the SDK will use to tint certain UI
     * elements in the SDK.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param colorString
     *        The value of the primary color
     */
    private void setPrimaryColor(final CallbackContext callbackContext, String colorString) {

        if (colorString != null) {
         try {
             int colorInt = Color.parseColor(colorString);
            Instabug.setPrimaryColor(colorInt);

         } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
         }

        } else callbackContext.error("A colorInt must be provided.");
    }

    /**
     * If your app already acquires the user's email address and you provide
     * it to this method, Instabug will pre-fill the user email in reports.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param email
     *        User's default email
     */
    @Deprecated
    private void setUserEmail(final CallbackContext callbackContext, String email) {
        if (email != null && email.length() > 0) {
            try {
                Instabug.setUserEmail(email);
                callbackContext.success();
            } catch (IllegalStateException e) {
                callbackContext.error(errorMsg);
            }
        } else callbackContext.error("An email must be provided.");
    }

    /**
     * Sets the user name that is used in the dashboard's contacts.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param name
     *        User's name
     */
    @Deprecated
    private void setUserName(final CallbackContext callbackContext, String name) {
        if (name != null && name.length() > 0) {
            try {
                Instabug.setUsername(name);
                callbackContext.success();
            } catch (IllegalStateException e) {
                callbackContext.error(errorMsg);
            }
        } else callbackContext.error("A name must be provided.");
    }

    /**
     * Set the user identity.
     * Instabug will pre-fill the user email in reports.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param email
     *        User's default email
     * @param name
     *        Username
     *
     */
    private void identifyUserWithEmail(final CallbackContext callbackContext, String email, String name) {
        if (name != null && name.length() > 0 && email != null && email.length() > 0) {
            try {
                Instabug.identifyUser(name, email);
                callbackContext.success();
            } catch (IllegalStateException e) {
                callbackContext.error(errorMsg);
            }
        } else callbackContext.error("A name and email must be provided.");
    }


    /**
     * Logout User
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    private void logOut(final CallbackContext callbackContext) {
        try {
            Instabug.logoutUser();
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Adds specific user data that you need to reports.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param data
     *        String representing user data.
     */
    private void setUserData(final CallbackContext callbackContext, String data) {
        if (data != null && data.length() > 0) {
            try {
                Instabug.setUserData(data);
                callbackContext.success();
            } catch (IllegalStateException e) {
                callbackContext.error(errorMsg);
            }
        } else callbackContext.error("User data must be provided.");
    }

     /**
      * Shows survey with a specific token.
      * Does nothing if there are no available surveys with that specific token.
      * Answered and cancelled surveys won't show up again.
      * @param surveyToken - A String with a survey token.
      *
      */
    private void showSurveyWithToken(final CallbackContext callbackContext, String surveyToken) {
        if (surveyToken != null && surveyToken.length() > 0) {
            try {
                Instabug.showSurvey(surveyToken);
                callbackContext.success();
            } catch (IllegalStateException e) {
                callbackContext.error(errorMsg);
            }
        } else callbackContext.error("Survey token must be provided.");
    }

    /**
     * Uploads the specified file along with upcoming reports.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param args
     *        URI of desired file to be uploaded
     */
    private void addFile(final CallbackContext callbackContext, JSONArray args) {
        String fileUri = null;

        String sFileUri = args.optString(0);
        if (sFileUri != null && sFileUri.length() > 0) {
            fileUri = sFileUri;
        } else {
            // File location may be different across platforms
            // and can be specified as such
            JSONObject fileUrisForPlatforms = args.optJSONObject(0);

            if (fileUrisForPlatforms != null) {
                fileUri = fileUrisForPlatforms.optString("android");
            }
        }

        if (fileUri != null && fileUri.length() > 0) {

            Uri uri = Uri.parse(fileUri);
            File file = new File(uri.getPath());

            if (file.exists()) {
                // If the file doesn't exist at the path specified,
                // we won't be able to notify the containing app when
                // Instabug API call fails, so we check ourselves.
                try {
                    Instabug.setFileAttachment(uri,file.getName());
                    callbackContext.success();
                } catch (IllegalStateException e) {
                    callbackContext.error(errorMsg);
                }
            } else callbackContext.error("File " + fileUri + " does not exist.");

        } else callbackContext.error("A local file URI must be provided.");
    }

    /**
     * Appends a log message to the Instabug internal log that is then sent
     * along the next uploaded report. All log messages are timestamped.
     * Logs aren't cleared per single application run. If you wish to reset
     * the logs, use clearLog().
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param log
     *        Log message
     */
    private void addLog(final CallbackContext callbackContext, String log) {
        if (log != null && log.length() > 0) {
            try {
                Instabug.log(log);
                callbackContext.success();
            } catch (IllegalStateException e) {
                callbackContext.error(errorMsg);
            }
        } else callbackContext.error("A log must be provided.");
    }

    /**
     * Clears Instabug internal log.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    private void clearLog(final CallbackContext callbackContext) {
        try {
            Instabug.clearLog();
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Change the event used to invoke Instabug SDK.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param event
     *        Event to be used to invoke SDK.
     */
    private void changeInvocationEvent(final CallbackContext callbackContext, String event) {
        InstabugInvocationEvent iEvent = parseInvocationEvent(event);

        if (iEvent != null) {
            try {
                Instabug.changeInvocationEvent(iEvent);
                callbackContext.success();
            } catch (IllegalStateException e) {
                callbackContext.error(errorMsg);
            }
        } else callbackContext.error("A valid event type must be provided.");
    }

    /**
     * Disables all Instabug functionality.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    private void disable(final CallbackContext callbackContext) {
        try {
            Instabug.disable();
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Enables all Instabug functionality.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    private void enable(final CallbackContext callbackContext) {
        try {
            Instabug.enable();
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * If Instabug is enabled.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    private void getIsEnabled(final CallbackContext callbackContext) {
        try {
            boolean enabled = Instabug.isEnabled();
            callbackContext.success(enabled ? 1 : 0);
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Enable/Disable debug logs from Instabug SDK
     *
     * @param isDebugEnabled whether debug logs should be printed or not into LogCat
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    private void setDebugEnabled(final CallbackContext callbackContext, boolean isDebugEnabled) {
        try {
            Instabug.setDebugEnabled(isDebugEnabled);
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Enable/Disable view hierarchy from Instabug SDK
     *
     * @param isEnabled whether view hierarchy should be enabled or not
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    private void setViewHierarchyEnabled(final CallbackContext callbackContext, boolean isEnabled) {
        try {
          if(isEnabled) {
            Instabug.setViewHierarchyState(Feature.State.ENABLED);
          } else {
            Instabug.setViewHierarchyState(Feature.State.DISABLED);
          }
          callbackContext.success();
        } catch (IllegalStateException e) {
          callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets whether the SDK is recording the screen or not.
     *
     * @param isEnabled A boolean to set auto screen recording to being enabled or disabled.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    private void setAutoScreenRecordingEnabled(final CallbackContext callbackContext, boolean isEnabled) {
        try {
          Instabug.setAutoScreenRecordingEnabled(isEnabled);
          callbackContext.success();
        } catch (IllegalStateException e) {
          callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets maximum auto screen recording video duration.
     *
     * @param duration maximum duration of the screen recording video seconds
     * The maximum duration is 30 seconds
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    private void setAutoScreenRecordingMaxDuration(final CallbackContext callbackContext, int duration) {
        try {
          int durationInMilli = duration * 1000;
          Instabug.setAutoScreenRecordingMaxDuration(durationInMilli);
          callbackContext.success();
        } catch (IllegalStateException e) {
          callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets user attribute to overwrite it's value or create a new one if it doesn't exist.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param key   the attribute
     * @param value the value
     *
     */
    private void setUserAttribute(final CallbackContext callbackContext, String key, String value) {
        try {
            Instabug.setUserAttribute(key,value);
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Removes user attribute if exists.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param key the attribute key as string
     *
     */
    private void removeUserAttribute(final CallbackContext callbackContext, String key) {
        try {
            Instabug.removeUserAttribute(key);
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }


    /**
     * Gets all saved user attributes.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    private void getAllUserAttributes(final CallbackContext callbackContext) {
        try {
            HashMap userAttributes = Instabug.getAllUserAttributes();
            JSONObject jsonUserAttributes = new JSONObject(userAttributes);
            callbackContext.success(jsonUserAttributes);
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Gets specific user attribute.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param key
     *        the attribute key as string
     *
     */
    private void getUserAttribute(final CallbackContext callbackContext, String key) {
        try {
            String userAttribute = Instabug.getUserAttribute(key);
            callbackContext.success(userAttribute);
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Returns true if the survey with a specific token was answered before.
     * Will return false if the token does not exist or if the survey was not answered before.
     * @param surveyToken - A String with a survey token.
     * @param callbackContext callback with argument as the desired value of the whether
     * the survey has been responded to or not.
     *
     */
    private void hasRespondedToSurveyWithToken(final CallbackContext callbackContext, String surveyToken) {
        try {
            boolean hasResponded = Instabug.hasRespondToSurvey(surveyToken);
            callbackContext.sendPluginResult(new PluginResult(Status.OK, hasResponded));
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets the default position at which the Instabug screen recording button will be shown.
     * Different orientations are already handled.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param corner
     *        the attribute position as string
     *
     */
    private void setVideoRecordingFloatingButtonPosition(final CallbackContext callbackContext, String corner) {
        try {
          InstabugVideoRecordingButtonCorner buttonCorner = parseInstabugVideoRecordingButtonCorner(corner);
          Instabug.setVideoRecordingFloatingButtonCorner(buttonCorner);
          callbackContext.success();
        } catch (IllegalStateException e) {
          callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets whether the extended bug report mode should be disabled, enabled with
     * required fields or enabled with optional fields.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param mode
     *        A string to disable the extended bug report mode, enable it with
     *        required or with optional fields
     */
    private void setExtendedBugReportMode(final CallbackContext callbackContext, String mode) {
        try {
          if(mode.equals("enabledWithRequiredFields")) {
            Instabug.setExtendedBugReportState(ExtendedBugReport.State.ENABLED_WITH_REQUIRED_FIELDS);
          } else if(mode.equals("enabledWithOptionalFields")) {
            Instabug.setExtendedBugReportState(ExtendedBugReport.State.ENABLED_WITH_OPTIONAL_FIELDS);
          } else if(mode.equals("disabled")) {
            Instabug.setExtendedBugReportState(ExtendedBugReport.State.DISABLED);
          }
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets whether user steps tracking is visual, non visual or disabled.
     * User Steps tracking is enabled by default if it's available
     * in your current plan.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param reproStepsMode
     *        A string to set user steps tracking to be enabled,
     *        non visual or disabled.
     */
    private void setReproStepsMode(final CallbackContext callbackContext, String reproStepsMode) {
        try {
          if(reproStepsMode.equals("enabledWithNoScreenshots")) {
            Instabug.setReproStepsState(State.ENABLED_WITH_NO_SCREENSHOTS);
          } else if(reproStepsMode.equals("enabled")) {
            Instabug.setReproStepsState(State.ENABLED);
          } else if(reproStepsMode.equals("disabled")) {
            Instabug.setReproStepsState(State.DISABLED);
          }
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }


    /**
     * Set after how many sessions should the dismissed survey would show again.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param sessionsCount
     *        number of sessions that the dismissed survey will be shown after.
     * @param daysCount
     *        number of days that the dismissed survey will show after.
     */
    private void setThresholdForReshowingSurveyAfterDismiss(final CallbackContext callbackContext, int sessionsCount, int daysCount) {
        if (Math.signum(sessionsCount) != - 1 && Math.signum(daysCount) != - 1) {
            try {
                Instabug.setThresholdForReshowingSurveyAfterDismiss(sessionsCount, daysCount);
                callbackContext.success();
            } catch (IllegalStateException e) {
                callbackContext.error(errorMsg);
            }
        } else callbackContext.error("Session count and days count must be provided.");
    }

    /**
     * Shows the UI for feature requests list
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    private void showFeatureRequests(final CallbackContext callbackContext) {
        try {
            Instabug.showFeatureRequests();
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Set Surveys welcome screen enabled, default value is false
     *
     *
     * @param shouldShow whether should show welcome screen Surveys before surveys or not
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    private void setShouldShowSurveysWelcomeScreen(final CallbackContext callbackContext, boolean shouldShow) {
        try {
            Instabug.setShouldShowSurveysWelcomeScreen(shouldShow);
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Adds intent extras for all options passed to activate().
     */
    private void applyOptions() {
        for (int i = 0; i < optionKeys.length; i++) {
            applyOption(optionKeys[i]);
        }
    }

    /**
     * Convenience method for setting intent extras where valid.
     *
     * @param key
     *        Name of option to be included
     */
    private void applyOption(String key) {
        String val = options.optString(key);

        if (val != null && val.length() > 0) {
            activationIntent.putExtra(key, val);
        }
    }

    /**
     * Convenience method for converting string to InstabugInvocationEvent.
     *
     * @param event
     *        String shortcode for event
     */
    public static InstabugInvocationEvent parseInvocationEvent(String event) {
        if ("shake".equals(event)) {
            return InstabugInvocationEvent.SHAKE;
        } else if ("button".equals(event)) {
            return InstabugInvocationEvent.FLOATING_BUTTON;
        } else if ("swipe".equals(event)) {
            return InstabugInvocationEvent.TWO_FINGER_SWIPE_LEFT;
        } else if ("screenshot".equals(event)) {
            return InstabugInvocationEvent.SCREENSHOT_GESTURE;
        }else if ("none".equals(event)) {
            return InstabugInvocationEvent.NONE;
        } else return null;
    }

    /**
     * Convenience method for converting string to InstabugInvocationMode.
     *
     * @param mode
     *        String shortcode for mode
     */
    public static InstabugInvocationMode parseInvocationMode(String mode) {
        if ("chat".equals(mode)) {
            return InstabugInvocationMode.NEW_CHAT;
        } else if ("chats".equals(mode)) {
            return InstabugInvocationMode.CHATS_LIST;
        } else if ("bug".equals(mode)) {
            return InstabugInvocationMode.NEW_BUG;
        } else if ("feedback".equals(mode)) {
            return InstabugInvocationMode.NEW_FEEDBACK;
        } else if ("options".equals(mode)) {
            return InstabugInvocationMode.PROMPT_OPTION;
        } else return null;
    }

    /**
     * Convenience method for converting string to InstabugVideoRecordingButtonCorner.
     *
     * @param position
     *        String shortcode for position
     */
    public static InstabugVideoRecordingButtonCorner parseInstabugVideoRecordingButtonCorner(String position) {
        if ("topLeft".equals(position)) {
            return InstabugVideoRecordingButtonCorner.TOP_LEFT;
        } else if ("topRight".equals(position)) {
            return InstabugVideoRecordingButtonCorner.TOP_RIGHT;
        } else if ("bottomLeft".equals(position)) {
            return InstabugVideoRecordingButtonCorner.BOTTOM_LEFT;
        } else if ("bottomRight".equals(position)) {
            return InstabugVideoRecordingButtonCorner.BOTTOM_RIGHT;
        } else return null;
    }

}
