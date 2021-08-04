package com.instabug.cordova.plugin;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.instabug.bug.BugReporting;
import com.instabug.bug.invocation.Option;
import com.instabug.chat.Chats;
import com.instabug.chat.Replies;
import com.instabug.cordova.plugin.util.Util;
import com.instabug.featuresrequest.ActionType;
import com.instabug.featuresrequest.FeatureRequests;
import com.instabug.library.Feature;
import com.instabug.library.Instabug;
import com.instabug.library.InstabugColorTheme;
import com.instabug.library.OnSdkDismissCallback;
import com.instabug.library.extendedbugreport.ExtendedBugReport;
import com.instabug.library.internal.module.InstabugLocale;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.instabug.bug.invocation.InvocationMode;
import com.instabug.library.invocation.OnInvokeCallback;
import com.instabug.library.invocation.util.InstabugVideoRecordingButtonPosition;
import com.instabug.library.logging.InstabugLog;
import com.instabug.library.model.Report;
import com.instabug.library.ui.onboarding.WelcomeMessage;
import com.instabug.library.visualusersteps.State;
import com.instabug.survey.OnDismissCallback;
import com.instabug.survey.OnShowCallback;
import com.instabug.survey.Survey;
import com.instabug.survey.Surveys;

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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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
    private final String[] optionKeys = { "emailRequired", "commentRequired", 
            "shakingThresholdAndroid", "floatingButtonEdge", "colorTheme",
            "floatingButtonOffset", "enableDebug", "enableConsoleLogs", 
            "enableInstabugLogs", "enableTrackingUserSteps", "enableUserData",
            "enableCrashReporting", "enableInAppMessaging", "enableIntroDialog", 
            "enableConversationSounds", "enablePushNotifications", 
            "enableSessionProfiler", "welcomeMessageMode" };

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
     * @param action          The action to execute.
     * @param args            The exec() arguments in JSON form.
     * @param callbackContext The callback context used when calling back into
     *                        JavaScript.
     * @return Whether the action was valid.
     */
    @Override
    public boolean execute(final String action, JSONArray args, final CallbackContext callbackContext) {
        try {
            Method[] methods = this.getClass().getMethods();
            for (Method method : methods) {
                if (action.equals(method.getName())) {
                    if (args.isNull(0)) {
                        method.invoke(this, callbackContext);
                    } else {
                        method.invoke(this, callbackContext, args);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public final void show(final CallbackContext callbackContext) {
        try {
            Instabug.show();
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    public void getUnreadRepliesCount(CallbackContext callbackContext) {
        callbackContext.success(Replies.getUnreadRepliesCount());
    }

    public final void setBugReportingEnabled(CallbackContext callbackContext, JSONArray args) {
        Boolean isEnabled = args.optBoolean(0);
        if (isEnabled) {
            BugReporting.setState(Feature.State.ENABLED);
        } else {
            BugReporting.setState(Feature.State.DISABLED);
        }
        callbackContext.success();
    }

    public final void setChatsEnabled(CallbackContext callbackContext, JSONArray args) {
        Boolean isEnabled = args.optBoolean(0);
        if (isEnabled) {
            Chats.setState(Feature.State.ENABLED);
        } else {
            Chats.setState(Feature.State.DISABLED);
        }
        callbackContext.success();
    }

    public final void setRepliesEnabled(CallbackContext callbackContext, JSONArray args) {
        Boolean isEnabled = args.optBoolean(0);
        if (isEnabled) {
            Replies.setState(Feature.State.ENABLED);
        } else {
            Replies.setState(Feature.State.DISABLED);
        }
        callbackContext.success();
    }

    public final void showChats(CallbackContext callbackContext) {
        Chats.show();
        callbackContext.success();
    }

    public final void showReplies(CallbackContext callbackContext) {
        Replies.show();
        callbackContext.success();
    }

    public final void hasChats(CallbackContext callbackContext) {
        if (Replies.hasChats()) {
            callbackContext.success();
        }
    }

    public final void setReportTypes(final CallbackContext callbackContext, JSONArray args) {
        JSONArray types = args.optJSONArray(0);
        final String[] stringArrayOfReportTypes = toStringArray(types);
        if (stringArrayOfReportTypes.length != 0) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        BugReporting.setReportTypes(Util.parseReportTypes(stringArrayOfReportTypes));
                        callbackContext.success();
                    } catch (Exception e) {
                        callbackContext.error(e.getMessage());
                    }
                }
            });
        }
    }
    
    public final void showBugReportingWithReportTypeAndOptions(CallbackContext callbackContext, JSONArray args) {
        String reportType = args.optString(0);
        JSONArray options = args.optJSONArray(1);
        boolean error = false;
        if (reportType.equals("bug")) {
            BugReporting.show(BugReporting.ReportType.BUG);
        } else if (reportType.equals("feedback")) {
            BugReporting.show(BugReporting.ReportType.FEEDBACK);
        } else if (reportType.equals("question")) {
            BugReporting.show(BugReporting.ReportType.QUESTION);
        } else {
            error = true;
            callbackContext.error("Invalid report type " + reportType);
        }
        if (!error) {
            if (options.length() > 0) {
                setInvocationOptions(callbackContext, options);
            } else {
                callbackContext.success();
            }
        }
        
    }

    /**
     * Creates intent to initialize Instabug.
     *
     * @param callbackContext Used when calling back into JavaScript
     */
    public void startWithToken(final CallbackContext callbackContext, JSONArray args) {
        activate(callbackContext, args);
    }

    /**
     * Creates intent to initialize Instabug.
     *
     * @param callbackContext Used when calling back into JavaScript
     */
    private void activate(final CallbackContext callbackContext, JSONArray args) {
        this.options = args.optJSONObject(2);
        if (options != null) {
            // Attach extras
            applyOptions();
        }
        try {
            Method method = Util.getMethod(Class.forName("com.instabug.library.util.InstabugDeprecationLogger"), "setBaseUrl", String.class);
            if (method != null) {
                method.invoke(null, "https://docs.instabug.com/docs/cordova-sdk-migration-guide");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        cordova.getActivity().startActivity(activationIntent);

        callbackContext.success();
    }

    /**
     * Sets the invocation options used when invoke Instabug SDK
     *
     * @param args .optJSONArray(0) the invocation options array
     */
    public void setInvocationOptions(final CallbackContext callbackContext, JSONArray args) {
        JSONArray options = args.optJSONArray(0);
        String[] stringArrayOfInvocationOptions = toStringArray(options);
        if(stringArrayOfInvocationOptions.length != 0) {
            try {
                ArrayList<Integer> invocationOptions = parseInvocationOptions(stringArrayOfInvocationOptions);
                BugReporting.setOptions(convertIntegers(invocationOptions));
            } catch (IllegalStateException e) {
                callbackContext.error(errorMsg);
            }
        } else callbackContext.error("A valid prompt option type must be provided.");
    }

    /**
     * Sets whether users are required to enter an email address or not when doing a certain action `IBGAction`.
     *
     * @param args .optBoolean(0) A boolean to indicate whether email field is required or not.
     *
     * @param  args .optJSONArray(1) Action types values
     */
    public void setEmailFieldRequiredForFeatureRequests(final CallbackContext callbackContext, JSONArray args) {
        Boolean isRequired = args.optBoolean(0);
        JSONArray actionTypes = args.optJSONArray(1);
        String[] stringArrayOfActionTypes = toStringArray(actionTypes);
        if(stringArrayOfActionTypes.length != 0) {
            try {
                ArrayList<Integer> actionTypesArray = parseActionTypes(stringArrayOfActionTypes);
                FeatureRequests.setEmailFieldRequired(isRequired, convertIntegers(actionTypesArray));

            } catch (IllegalStateException e) {
                callbackContext.error(errorMsg);
            }
        } else callbackContext.error("A valid action type must be provided.");
    }


    /**
     * Sets a block of code to be executed just before the SDK's UI is presented.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    public void setPreInvocationHandler(final CallbackContext callbackContext) {
        try {
            BugReporting.setOnInvokeCallback(new OnInvokeCallback() {
                @Override
                public void onInvoke() {
                    PluginResult result = new PluginResult(PluginResult.Status.OK);
                    result.setKeepCallback(true);
                    callbackContext.sendPluginResult(result);
                }
            });
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets a block of code to be executed just before the SDK's UI is presented.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    public void setPreSendingHandler(final CallbackContext callbackContext) {
        try {
            Instabug.onReportSubmitHandler(new Report.OnReportCreatedListener() {
                @Override
                public void onReportCreated(Report report) {
                    JSONObject reportParam = new JSONObject();
                    try {
                        reportParam.put("tagsArray", report.getTags());
                        reportParam.put("consoleLogs", report.getConsoleLog());
                        reportParam.put("userData", report.getUserData());
                        reportParam.put("userAttributes", report.getUserAttributes());
                        reportParam.put("fileAttachments", report.getFileAttachments());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    PluginResult result = new PluginResult(PluginResult.Status.OK, reportParam);
                    result.setKeepCallback(true);
                    callbackContext.sendPluginResult(result);
                }
            });
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets a block of code to be executed just before the survey's UI is presented.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    public void willShowSurveyHandler(final CallbackContext callbackContext) {
        try {
            Surveys.setOnShowCallback(new OnShowCallback() {
                @Override
                public void onShow() {
                    PluginResult result = new PluginResult(PluginResult.Status.OK);
                    result.setKeepCallback(true);
                    callbackContext.sendPluginResult(result);
                }
            });
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets a block of code to be executed right after the survey's UI is dismissed.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    public void didDismissSurveyHandler(final CallbackContext callbackContext) {
        try {
            Surveys.setOnDismissCallback(new OnDismissCallback() {
                @Override
                public void onDismiss() {
                    PluginResult result = new PluginResult(PluginResult.Status.OK);
                    result.setKeepCallback(true);
                    callbackContext.sendPluginResult(result);
                }
            });
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets a block of code to be executed right after the SDK's UI is dismissed.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    public void setPostInvocationHandler(final CallbackContext callbackContext) {
        try {
            BugReporting.setOnDismissCallback(new OnSdkDismissCallback() {
                @Override
                public void call(DismissType dismissType, ReportType reportType) {
                    JSONObject parameters = new JSONObject();
                    try {
                        parameters.put("dismissType", dismissType.toString());
                        parameters.put("reportType", reportType.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    PluginResult result = new PluginResult(PluginResult.Status.OK, parameters);
                    result.setKeepCallback(true);
                    callbackContext.sendPluginResult(result);
                }
            });
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets a block of code to be executed right after the SDK's UI is dismissed.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    public void getAvailableSurveys(final CallbackContext callbackContext) {
        try {
            JSONArray surveysArray = toJson(Surveys.getAvailableSurveys());
            PluginResult result = new PluginResult(Status.OK, surveysArray);
            callbackContext.sendPluginResult(result);
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Set the primary color that the SDK will use to tint certain UI
     * elements in the SDK.
     * Set the primary color that the SDK will use to tint certain UI elements in
     * the SDK.
     *
     * @param callbackContext Used when calling back into JavaScript
     * @param args .optString(0)     The value of the primary color
     */
    public void setPrimaryColor(final CallbackContext callbackContext, final JSONArray args) {
        final String colorString = args.optString(0);
        if (colorString != null) {
            try {
                final int colorInt = Color.parseColor(colorString);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Instabug.setPrimaryColor(colorInt);
                    }
                });


            } catch (IllegalStateException e) {
                e.printStackTrace();
                callbackContext.error(errorMsg);
            }

        } else
            callbackContext.error("A colorInt must be provided.");
    }

    /**
     * Convenience method for parsing and setting
     * whether the desired color theme for the SDK
     * invocation.
     *
     * @param callbackContext Used when calling back into JavaScript
     * @param args .optString(0)     Theme
     *
     */
    public void setColorTheme(final CallbackContext callbackContext, final JSONArray args) {
        final String colorTheme = args.optString(0);
        if (colorTheme != null) {
            try {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if ("dark".equals(colorTheme)) {
                            Instabug.setColorTheme(InstabugColorTheme.InstabugColorThemeDark);
                        } else if ("light".equals(colorTheme)) {
                            Instabug.setColorTheme(InstabugColorTheme.InstabugColorThemeLight);
                        } else {
                            callbackContext.error("Color theme value is not valid.");
                        }
                    }
                });
            } catch (IllegalStateException e) {
                e.printStackTrace();
                callbackContext.error(errorMsg);
            }
        } else {
            callbackContext.error("A color theme must be provided.");
        }
    }

    /**
     * Sets the threshold value of the shake gesture on the device
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param args .optInt(0)
     *        The value of the primary color
     */
    public void setShakingThreshold(final CallbackContext callbackContext, JSONArray args) {
        int shakingThreshold = args.optInt(0);
         try {
             BugReporting.setShakingThreshold(shakingThreshold);
         } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
         }
    }

    /**
     * Set the user identity.
     * Instabug will pre-fill the user email in reports.
     * Set the user identity. Instabug will pre-fill the user email in reports.
     *
     * @param callbackContext Used when calling back into JavaScript
     * @param args .optString(0)           User's default email
     * @param args .optString(1)            Username
     *
     */
    public void identifyUserWithEmail(final CallbackContext callbackContext, JSONArray args) {
        String email = args.optString(0);
        String name = args.optString(1);
        if (name != null && name.length() > 0 && email != null && email.length() > 0) {
            try {
                Instabug.identifyUser(name, email);
                callbackContext.success();
            } catch (IllegalStateException e) {
                callbackContext.error(errorMsg);
            }
        } else
            callbackContext.error("A name and email must be provided.");
    }

    /**
     * Logout User
     *
     * @param callbackContext Used when calling back into JavaScript
     */
    public void logOut(final CallbackContext callbackContext) {
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
     * @param callbackContext Used when calling back into JavaScript
     * @param args .optString(0);            String representing user data.
     */
    public void setUserData(final CallbackContext callbackContext, JSONArray args) {
        String data = args.optString(0);
        if (data != null && data.length() > 0) {
            try {
                Instabug.setUserData(data);
                callbackContext.success();
            } catch (IllegalStateException e) {
                callbackContext.error(errorMsg);
            }
        } else
            callbackContext.error("User data must be provided.");
    }

    /**
     * Shows survey with a specific token. Does nothing if there are no available
     * surveys with that specific token. Answered and cancelled surveys won't show
     * up again.
     * 
     * @param args .optString(0) - A String with a survey token.
     *
     */
    public void showSurveyWithToken(final CallbackContext callbackContext, JSONArray args) {
        String surveyToken = args.optString(0);
        if (surveyToken != null && surveyToken.length() > 0) {
            try {
                Surveys.showSurvey(surveyToken);
                callbackContext.success();
            } catch (IllegalStateException e) {
                callbackContext.error(errorMsg);
            }
        } else
            callbackContext.error("Survey token must be provided.");
    }

    /**
     * Uploads the specified file along with upcoming reports.
     *
     * @param callbackContext Used when calling back into JavaScript
     * @param args            URI of desired file to be uploaded
     */
    public void addFile(final CallbackContext callbackContext, JSONArray args) {
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
                    
                    Instabug.addFileAttachment(uri, file.getName());
                    callbackContext.success();
                } catch (IllegalStateException e) {
                    callbackContext.error(errorMsg);
                }
            } else
                callbackContext.error("File " + fileUri + " does not exist.");

        } else
            callbackContext.error("A local file URI must be provided.");
    }

    /**
     * Appends a log message to the Instabug internal log that is then sent along
     * the next uploaded report. All log messages are timestamped. Logs aren't
     * cleared per single application run. If you wish to reset the logs, use
     * clearLog().
     *
     * @param callbackContext Used when calling back into JavaScript
     * @param args .optString(0)             Log message
     */
    public void addLog(final CallbackContext callbackContext, JSONArray args) {
        String log = args.optString(0);
        if (log != null && log.length() > 0) {
            try {
                InstabugLog.d(log);
                callbackContext.success();
            } catch (IllegalStateException e) {
                callbackContext.error(errorMsg);
            }
        } else
            callbackContext.error("A log must be provided.");
    }

    /**
     * Clears Instabug internal log.
     *
     * @param callbackContext Used when calling back into JavaScript
     */
    public void clearLog(final CallbackContext callbackContext) {
        try {
            InstabugLog.clearLogs();
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
     * @param args .optJSONArray(0)
     *        Event to be used to invoke SDK.
     */
    public void setInvocationEvents(final CallbackContext callbackContext, JSONArray args) {
        JSONArray events = args.optJSONArray(0);
        String[] stringArrayOfEvents = toStringArray(events);
        final ArrayList<InstabugInvocationEvent> invocationEvents = new ArrayList<InstabugInvocationEvent>();
        if(stringArrayOfEvents.length != 0) {
            try {
                for (String event : stringArrayOfEvents) {
                    InstabugInvocationEvent iEvent = parseInvocationEvent(event);
                    if(iEvent != null) {
                        switch (iEvent) {
                            case SHAKE:
                                invocationEvents.add(InstabugInvocationEvent.SHAKE);
                                break;
                            case FLOATING_BUTTON:
                                invocationEvents.add(InstabugInvocationEvent.FLOATING_BUTTON);
                                break;
                            case TWO_FINGER_SWIPE_LEFT:
                                invocationEvents.add(InstabugInvocationEvent.TWO_FINGER_SWIPE_LEFT);
                                break;
                            case SCREENSHOT:
                                invocationEvents.add(InstabugInvocationEvent.SCREENSHOT);
                                break;
                            case NONE:
                                invocationEvents.add(InstabugInvocationEvent.NONE);
                                break;
                            default:
                                invocationEvents.add(InstabugInvocationEvent.SHAKE);
                                break;
                        }
                    } else {
                        callbackContext.error("A valid event type must be provided.");
                    }
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        BugReporting.setInvocationEvents(invocationEvents.toArray(new InstabugInvocationEvent[0]));
                    }
                });
            } catch (IllegalStateException e) {
                callbackContext.error(errorMsg);
            }
        } else callbackContext.error("A valid event type must be provided.");
    }

    /**
     * Convenience method to parse string array of invocation options into an Arraylist of integers
     *
     * @param invocationOptionsStringArray
     *        string array of invocation options
     */
    private ArrayList<Integer> parseInvocationOptions(String[] invocationOptionsStringArray) {
        ArrayList<Integer> invocationOptions = new ArrayList<Integer>();
        if(invocationOptionsStringArray.length != 0) {
            for (String option : invocationOptionsStringArray) {
                int iOption = parseInvocationOption(option);
                if (iOption != -1) {
                    invocationOptions.add(iOption);
                }
            }
        }
        return invocationOptions;
    }

    /**
     * Convenience method to parse string array of invocation options into an Arraylist of integers
     *
     * @param actionTypesStringArray
     *        string array of invocation options
     */
    private ArrayList<Integer> parseActionTypes(String[] actionTypesStringArray) {
        ArrayList<Integer> actionTypes = new ArrayList<Integer>();
        if(actionTypesStringArray.length != 0) {
            for (String actionType : actionTypesStringArray) {
                int aType = parseActionType(actionType);
                if (aType != -1) {
                    actionTypes.add(aType);
                }
            }
        }
        return actionTypes;
    }

    /**
     * Convenience method to convert from JSON array to string array.
     *
     * @param array
     *        JSONArray to be converted to string.
     */
    private static String[] toStringArray(JSONArray array) {
        if(array==null)
            return null;

        String[] arr=new String[array.length()];
        for(int i=0; i<arr.length; i++) {
            arr[i]=array.optString(i);
        }

        return arr;
    }

    /**
     * Disables all Instabug functionality.
     *
     * @param callbackContext Used when calling back into JavaScript
     */
    public void disable(final CallbackContext callbackContext) {
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
     * @param callbackContext Used when calling back into JavaScript
     */
    public void enable(final CallbackContext callbackContext) {
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
     * @param callbackContext Used when calling back into JavaScript
     */
    public void getIsEnabled(final CallbackContext callbackContext) {
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
     * @param args .optBoolean(0)  whether debug logs should be printed or not into
     *                        LogCat
     *
     * @param callbackContext Used when calling back into JavaScript
     */
    public void setDebugEnabled(final CallbackContext callbackContext, JSONArray args) {
        Boolean isDebugEnabled = args.optBoolean(0);
        try {
            Instabug.setDebugEnabled(isDebugEnabled);
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets whether auto surveys showing are enabled or not.
     *
     * @param args .optBoolean(0) whether debug logs should be printed or not into LogCat
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    public void setAutoShowingSurveysEnabled(final CallbackContext callbackContext, JSONArray args) {
        Boolean isEnabled = args.optBoolean(0);
        try {
            Surveys.setAutoShowingEnabled(isEnabled);
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Enables/disables showing in-app notifications when the user receives a new
     * message.
     *
     * @param isEnabled whether debug logs should be printed or not into LogCat
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    public void setChatNotificationEnabled(final CallbackContext callbackContext, boolean isEnabled) {
        try {
            Replies.setInAppNotificationEnabled(isEnabled);
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Enable/Disable view hierarchy from Instabug SDK
     *
     * @param args .optBoolean(0)       whether view hierarchy should be enabled or not
     *
     * @param callbackContext Used when calling back into JavaScript
     */
    public void setViewHierarchyEnabled(final CallbackContext callbackContext, JSONArray args) {
        Boolean isEnabled = args.optBoolean(0);
        try {
            if (isEnabled) {
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
     * Shows one of the surveys that were not shown before, that also have conditions that
     * match the current device/user.
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    public void showSurveyIfAvailable(final CallbackContext callbackContext) {
        try {
            Surveys.showSurveyIfAvailable();
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets maximum auto screen recording video duration.
     *
     * @param args .optInt(0)        maximum duration of the screen recording video seconds
     *                        The maximum duration is 30 seconds
     *
     * @param callbackContext Used when calling back into JavaScript
     */
    public void setAutoScreenRecordingMaxDuration(final CallbackContext callbackContext, JSONArray args) {
        int duration = args.optInt(0);
        try {
            int durationInMilli = duration * 1000;
            Instabug.setAutoScreenRecordingMaxDuration(durationInMilli);
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets user attribute to overwrite it's value or create a new one if it doesn't
     * exist.
     *
     * @param callbackContext Used when calling back into JavaScript
     * @param args .optString(0);             the attribute
     * @param args .optString(1);           the value
     *
     */
    public void setUserAttribute(final CallbackContext callbackContext, JSONArray args) {
        String key = args.optString(0);
        String value = args.optString(1);
        try {
            Instabug.setUserAttribute(key, value);
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Removes user attribute if exists.
     *
     * @param callbackContext Used when calling back into JavaScript
     * @param args .optString(0)             the attribute key as string
     *
     */
    public void removeUserAttribute(final CallbackContext callbackContext, JSONArray args) {
        String key = args.optString(0);
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
     * @param callbackContext Used when calling back into JavaScript
     */
    public void getAllUserAttributes(final CallbackContext callbackContext) {
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
     * @param callbackContext Used when calling back into JavaScript
     * @param  args .optString(0)             the attribute key as string
     *
     */
    public void getUserAttribute(final CallbackContext callbackContext, JSONArray args) {
        String key = args.optString(0);
        try {
            String userAttribute = Instabug.getUserAttribute(key);
            callbackContext.success(userAttribute);
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Returns true if the survey with a specific token was answered before. Will
     * return false if the token does not exist or if the survey was not answered
     * before.
     * 
     * @param args .optString(0)     - A String with a survey token.
     * @param callbackContext callback with argument as the desired value of the
     *                        whether the survey has been responded to or not.
     *
     */
    public void hasRespondedToSurveyWithToken(final CallbackContext callbackContext, JSONArray args) {
        String surveyToken = args.optString(0);
        try {
            boolean hasResponded = Surveys.hasRespondToSurvey(surveyToken);
            callbackContext.sendPluginResult(new PluginResult(Status.OK, hasResponded));
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets the default position at which the Instabug screen recording button will
     * be shown. Different orientations are already handled.
     *
     * @param callbackContext Used when calling back into JavaScript
     * @param args .optString(0)          the attribute position as string
     *
     */
    public void setVideoRecordingFloatingButtonPosition(final CallbackContext callbackContext, JSONArray args) {
        String corner = args.optString(0);
        try {
          InstabugVideoRecordingButtonPosition buttonPosition = parseInstabugVideoRecordingButtonCorner(corner);
          BugReporting.setVideoRecordingFloatingButtonPosition(buttonPosition);
          callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets whether the extended bug report mode should be disabled, enabled with
     * required fields or enabled with optional fields.
     *
     * @param callbackContext Used when calling back into JavaScript
     * @param args .optString(0)            A string to disable the extended bug report mode,
     *                        enable it with required or with optional fields
     */
    public void setExtendedBugReportMode(final CallbackContext callbackContext, JSONArray args) {
        String mode = args.optString(0);
        try {
          if(mode.equals("enabledWithRequiredFields")) {
              BugReporting.setExtendedBugReportState(ExtendedBugReport.State.ENABLED_WITH_REQUIRED_FIELDS);
          } else if(mode.equals("enabledWithOptionalFields")) {
              BugReporting.setExtendedBugReportState(ExtendedBugReport.State.ENABLED_WITH_OPTIONAL_FIELDS);
          } else if(mode.equals("disabled")) {
              BugReporting.setExtendedBugReportState(ExtendedBugReport.State.DISABLED);
          }
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Sets whether user steps tracking is visual, non visual or disabled. User
     * Steps tracking is enabled by default if it's available in your current plan.
     *
     * @param callbackContext Used when calling back into JavaScript
     * @param args .optString(0)  A string to set user steps tracking to be enabled, non
     *                        visual or disabled.
     */
    public void setReproStepsMode(final CallbackContext callbackContext, JSONArray args) {
        String reproStepsMode = args.optString(0);
        try {
            if (reproStepsMode.equals("enabledWithNoScreenshots")) {
                Instabug.setReproStepsState(State.ENABLED_WITH_NO_SCREENSHOTS);
            } else if (reproStepsMode.equals("enabled")) {
                Instabug.setReproStepsState(State.ENABLED);
            } else if (reproStepsMode.equals("disabled")) {
                Instabug.setReproStepsState(State.DISABLED);
            }
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Shows the welcome message in a specific mode.
     *
     * @param callbackContext    Used when calling back into JavaScript
     * @param  args .optString(0) A string to set user steps tracking to be enabled,
     *                           non visual or disabled.
     */
    public void showWelcomeMessage(final CallbackContext callbackContext, JSONArray args) {
        String welcomeMessageMode = args.optString(0);
        try {
            if (welcomeMessageMode.equals("welcomeMessageModeLive")) {
                Instabug.showWelcomeMessage(WelcomeMessage.State.LIVE);
            } else if (welcomeMessageMode.equals("welcomeMessageModeBeta")) {
                Instabug.showWelcomeMessage(WelcomeMessage.State.BETA);
            } else {
                Instabug.showWelcomeMessage(WelcomeMessage.State.LIVE);
            }
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Set after how many sessions should the dismissed survey would show again.
     *
     * @param callbackContext Used when calling back into JavaScript
     * @param args .optInt(0)   number of sessions that the dismissed survey will be
     *                        shown after.
     * @param args .optInt(1)       number of days that the dismissed survey will show
     *                        after.
     */
    public void setThresholdForReshowingSurveyAfterDismiss(final CallbackContext callbackContext, JSONArray args) {
        int sessionsCount = args.optInt(0);
        int daysCount = args.optInt(1);
        if (Math.signum(sessionsCount) != -1 && Math.signum(daysCount) != -1) {
            try {
                Surveys.setThresholdForReshowingSurveyAfterDismiss(sessionsCount, daysCount);
                callbackContext.success();
            } catch (IllegalStateException e) {
                callbackContext.error(errorMsg);
            }
        } else
            callbackContext.error("Session count and days count must be provided.");
    }

    /**
     * Shows the UI for feature requests list
     *
     * @param callbackContext Used when calling back into JavaScript
     */
    public void showFeatureRequests(final CallbackContext callbackContext) {
        try {
            FeatureRequests.show();
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Set Surveys welcome screen enabled, default value is false
     *
     *
     * @param args .optBoolean(0)      whether should show welcome screen Surveys before
     *                        surveys or not
     *
     * @param callbackContext Used when calling back into JavaScript
     */
    public void setShouldShowSurveysWelcomeScreen(final CallbackContext callbackContext, JSONArray args) {
        Boolean shouldShow = args.optBoolean(0);
        try {
            Surveys.setShouldShowWelcomeScreen(shouldShow);
            callbackContext.success();
        } catch (IllegalStateException e) {
            callbackContext.error(errorMsg);
        }
    }

    /**
     * Change Locale of Instabug UI elements(defaults to English)
     *
     *
     * @param args .optString(0)      locale name
     *
     * @param callbackContext Used when calling back into JavaScript
     */
    public void setLocale(final CallbackContext callbackContext, JSONArray args) {
        String locale = args.optString(0);
        try {
            InstabugLocale instabugLocale = parseLocale(locale);
            Instabug.setLocale(new Locale(instabugLocale.getCode(), instabugLocale.getCountry()));
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
     * @param key Name of option to be included
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
     * @param event String shortcode for event
     */
    public static InstabugInvocationEvent parseInvocationEvent(String event) {
        if ("shake".equals(event)) {
            return InstabugInvocationEvent.SHAKE;
        } else if ("button".equals(event)) {
            return InstabugInvocationEvent.FLOATING_BUTTON;
        } else if ("swipe".equals(event)) {
            return InstabugInvocationEvent.TWO_FINGER_SWIPE_LEFT;
        } else if ("screenshot".equals(event)) {
            return InstabugInvocationEvent.SCREENSHOT;
        } else if ("none".equals(event)) {
            return InstabugInvocationEvent.NONE;
        } else
            return null;
    }

    /**
     * Convenience method for converting string to InvocationOption.
     *
     * @param invocationOption
     *        String shortcode for prompt option
     */
    public static int parseInvocationOption(String invocationOption) {
        if ("emailFieldHidden".equals(invocationOption)) {
            return Option.EMAIL_FIELD_HIDDEN;
        } else if ("emailFieldOptional".equals(invocationOption)) {
            return Option.EMAIL_FIELD_OPTIONAL;
        } else if ("commentFieldRequired".equals(invocationOption)) {
            return Option.COMMENT_FIELD_REQUIRED;
        } else if ("disablePostSendingDialog".equals(invocationOption)) {
            return Option.DISABLE_POST_SENDING_DIALOG;
        } else return -1;
    }

    /**
     * Convenience method for converting string to Locale.
     *
     * @param locale
     *        String shortcode for locale
     */
    public static InstabugLocale parseLocale(String locale) {
        if ("arabic".equals(locale)) {
            return InstabugLocale.ARABIC;
        } else if ("azerbaijani".equals(locale)) {
            return InstabugLocale.AZERBAIJANI;
        } else if ("chineseSimplified".equals(locale)) {
            return InstabugLocale.SIMPLIFIED_CHINESE;
        } else if ("chineseTraditional".equals(locale)) {
            return InstabugLocale.TRADITIONAL_CHINESE;
        } else if ("danish".equals(locale)) {
            return InstabugLocale.DANISH;
        } else if ("english".equals(locale)) {
            return InstabugLocale.ENGLISH;
        } else if ("french".equals(locale)) {
            return InstabugLocale.FRENCH;
        } else if ("german".equals(locale)) {
            return InstabugLocale.GERMAN;
        } else if ("italian".equals(locale)) {
            return InstabugLocale.ITALIAN;
        } else if ("japanese".equals(locale)) {
            return InstabugLocale.JAPANESE;
        } else if ("korean".equals(locale)) {
            return InstabugLocale.KOREAN;
        } else if ("polish".equals(locale)) {
            return InstabugLocale.POLISH;
        } else if ("portugueseBrazil".equals(locale)) {
            return InstabugLocale.PORTUGUESE_BRAZIL;
        } else if ("russian".equals(locale)) {
            return InstabugLocale.RUSSIAN;
        } else if ("spanish".equals(locale)) {
            return InstabugLocale.SPANISH;
        } else if ("swedish".equals(locale)) {
            return InstabugLocale.SWEDISH;
        } else if ("turkish".equals(locale)) {
            return InstabugLocale.TURKISH;
        } else if ("czech".equals(locale)) {
            return InstabugLocale.CZECH;
        } else return InstabugLocale.ENGLISH;
    }

    /**
     * Convenience method for converting string to InvocationOption.
     *
     * @param actionType
     *        String shortcode for prompt option
     */
    public static int parseActionType(String actionType) {
        if ("requestNewFeature".equals(actionType)) {
            return ActionType.REQUEST_NEW_FEATURE;
        } else if ("addCommentToFeature".equals(actionType)) {
            return ActionType.ADD_COMMENT_TO_FEATURE;
        } else return -1;
    }

    /**
     * Convenience method for converting string to InstabugInvocationMode.
     *
     * @param mode String shortcode for mode
     */
    public static InvocationMode parseInvocationMode(String mode) {
        if ("chat".equals(mode)) {
            return InvocationMode.NEW_CHAT;
        } else if ("chats".equals(mode)) {
            return InvocationMode.CHATS_LIST;
        } else if ("bug".equals(mode)) {
            return InvocationMode.NEW_BUG;
        } else if ("feedback".equals(mode)) {
            return InvocationMode.NEW_FEEDBACK;
        } else if ("options".equals(mode)) {
            return InvocationMode.PROMPT_OPTION;
        } else return null;
    }

    /**
     * Convenience method for converting string to
     * InstabugVideoRecordingButtonCorner.
     *
     * @param position String shortcode for position
     */
    public static InstabugVideoRecordingButtonPosition parseInstabugVideoRecordingButtonCorner(String position) {
        if ("topLeft".equals(position)) {
            return InstabugVideoRecordingButtonPosition.TOP_LEFT;
        } else if ("topRight".equals(position)) {
            return InstabugVideoRecordingButtonPosition.TOP_RIGHT;
        } else if ("bottomLeft".equals(position)) {
            return InstabugVideoRecordingButtonPosition.BOTTOM_LEFT;
        } else if ("bottomRight".equals(position)) {
            return InstabugVideoRecordingButtonPosition.BOTTOM_RIGHT;
        } else return null;
    }


    /**
     * Convenience method to convert a list of integers into an int array
     *
     * @param integers
     *        list of integers to be converted
     */
    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    /**
     * Convenience method to convert from a list of Surveys to a JSON array
     *
     * @param list
     *        List of Surveys to be converted to JSON array
     */
    public static JSONArray toJson(List<Survey> list) {
        JSONArray jsonArray = new JSONArray();
        try{
            for (Survey obj : list) {
                JSONObject object = new JSONObject();
                object.put("title", obj.getTitle());
                jsonArray.put(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
