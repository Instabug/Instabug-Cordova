package com.instabug.cordova.plugin;

import android.content.Intent;
import android.net.Uri;

import com.instabug.library.IBGInvocationEvent;
import com.instabug.library.IBGInvocationMode;
import com.instabug.library.Instabug;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

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
        "defaultInvocationMode",
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
        "colorTheme" 
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
     * @param command
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

        } else if ("disable".equals(action)) {
            disable(callbackContext);
            
        } else if ("enable".equals(action)) {
            enable(callbackContext);
            
        } else if ("isEnabled".equals(action)) {
            getIsEnabled(callbackContext);
            
        } else if ("isInvoked".equals(action)) {
            getIsInvoked(callbackContext);
            
        } else if ("isDebugEnabled".equals(action)) {
            getIsDebugEnabled(callbackContext);
              
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
        JSONObject tokensForPlatforms = args.optJSONObject(0);

        if (tokensForPlatforms != null) {
            String token = tokensForPlatforms.optString("android");

            if (token != null && token.length() > 0) {
                activationIntent.putExtra("token", token);

                String invocationEvent = args.optString(1);

                if (invocationEvent != null && invocationEvent.length() > 0) {
                    activationIntent.putExtra("invocationEvent", invocationEvent);
                } else {
                    // Desired invocation event may be different across platforms
                    // and can be specified as such
                    JSONObject invEventsForPlatforms = args.optJSONObject(1);

                    if (invEventsForPlatforms != null) {
                        String invEvent = invEventsForPlatforms.optString("android");

                        if (invEvent != null && invEvent.length() > 0) {
                            activationIntent.putExtra("invocationEvent", invEvent);
                        }
                    }
                }

                this.options = args.optJSONObject(2);

                if (options != null) {
                    // Attach extras
                    applyOptions();
                }

                // Start activity to initialize Instabug
                cordova.getActivity().startActivity(activationIntent);

                callbackContext.success();

            } else {
                // Without a token, Instabug cannot be initialized.
                callbackContext.error("An application token must be provided.");
            }
        } else {
            // Without a token, Instabug cannot be initialized.
            callbackContext.error("An application token must be provided.");
        }


        
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
        IBGInvocationMode iMode = parseInvocationMode(mode);

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
     * @param color
     *        The value of the primary color
     */
    private void setPrimaryColor(final CallbackContext callbackContext, String color) {
        if (color != null && color.length() > 0) {
            boolean valid = false;
            String hColor = null;

            if (color.length() == 6) {
                valid = true;
                hColor = color;
            } else if (color.length() == 7 && color.indexOf("#") == 0) {
                valid = true;
                // '#' char must be removed before parsing
                hColor = color.substring(1);
            }

            if (valid) {
                int hexColor = Integer.parseInt(hColor, 16)+0xFF000000;

                try {
                    Instabug.setPrimaryColor(hexColor);
                } catch (IllegalStateException e) {
                    callbackContext.error(errorMsg);
                }
            } else callbackContext.error(color + "is not a valid hex color.");

            
        } else callbackContext.error("A hex color must be provided.");
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
     * Uploads the specified file along with upcoming reports.
     * 
     * @param callbackContext 
     *        Used when calling back into JavaScript
     * @param fileUri
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
        IBGInvocationEvent iEvent = parseInvocationEvent(event);

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
     * If Instabug is currently invoked (shown).
     * 
     * @param callbackContext 
     *        Used when calling back into JavaScript
     */
    private void getIsInvoked(final CallbackContext callbackContext) {
        try {
            boolean invoked = Instabug.isSDKInvoked();
            callbackContext.success(invoked ? 1 : 0);
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * If Instabug SDK debug logs will be printed to LogCat.
     * 
     * @param callbackContext 
     *        Used when calling back into JavaScript
     */
    private void getIsDebugEnabled(final CallbackContext callbackContext) {
        try {
            boolean enabled = Instabug.isDebugEnabled();
            callbackContext.success(enabled ? 1 : 0);
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
     * Convenience method for converting string to IBGInvocationEvent.
     * 
     * @param event
     *        String shortcode for event
     */
    public static IBGInvocationEvent parseInvocationEvent(String event) {
        if ("shake".equals(event)) {
            return IBGInvocationEvent.IBGInvocationEventShake;
        } else if ("button".equals(event)) {
            return IBGInvocationEvent.IBGInvocationEventFloatingButton;
        } else if ("swipe".equals(event)) {
            return IBGInvocationEvent.IBGInvocationEventTwoFingersSwipeLeft;
        } else if ("none".equals(event)) {
            return IBGInvocationEvent.IBGInvocationEventNone;
        } else return null;
    }

    /**
     * Convenience method for converting string to IBGInvocationMode.
     * 
     * @param mode
     *        String shortcode for mode
     */
    public static IBGInvocationMode parseInvocationMode(String mode) {
        if ("bug".equals(mode)) {
            return IBGInvocationMode.IBGInvocationModeBugReporter;
        } else if ("feedback".equals(mode)) {
            return IBGInvocationMode.IBGInvocationModeFeedbackSender;
        } else if ("na".equals(mode)) {
            return IBGInvocationMode.IBGInvocationModeNA;
        } else return null;
    }

}