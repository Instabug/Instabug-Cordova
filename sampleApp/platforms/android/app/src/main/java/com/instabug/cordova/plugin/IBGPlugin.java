package com.instabug.cordova.plugin;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.instabug.bug.BugReporting;
import com.instabug.bug.invocation.InvocationOption;
import com.instabug.bug.invocation.Option;
import com.instabug.chat.Chats;
import com.instabug.chat.Replies;
import com.instabug.cordova.plugin.util.Util;
import com.instabug.featuresrequest.ActionType;
import com.instabug.featuresrequest.FeatureRequests;
import com.instabug.library.Feature;
import com.instabug.library.Instabug;
import com.instabug.library.OnSdkDismissCallback;
import com.instabug.library.extendedbugreport.ExtendedBugReport;
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
    public boolean execute(final String action, JSONArray args, final CallbackContext callbackContext)
            throws JSONException {

        if ("activate".equals(action)) {
            activate(callbackContext, args);

        } if ("startWithToken".equals(action)) {
            activate(callbackContext, args);

        } else if ("setPrimaryColor".equals(action)) {
            setPrimaryColor(callbackContext, args.optString(0));

        } else if ("setUserData".equals(action)) {
            setUserData(callbackContext, args.optString(0));

        } else if ("addFile".equals(action)) {
            addFile(callbackContext, args);

        } else if ("addLog".equals(action)) {
            addLog(callbackContext, args.optString(0));

        } else if ("clearLog".equals(action)) {
            clearLog(callbackContext);

        } else if ("setInvocationEvents".equals(action)) {
            setInvocationEvents(callbackContext, args.optJSONArray(0));
            
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

        } else if ("setPreInvocationHandler".equals(action)) {
            setPreInvocationHandler(callbackContext);

        } else if ("setPreSendingHandler".equals(action)) {
            setPreSendingHandler(callbackContext);

        } else if ("setPostInvocationHandler".equals(action)) {
            setPostInvocationHandler(callbackContext);

        } else if ("didDismissSurveyHandler".equals(action)) {
            didDismissSurveyHandler(callbackContext);

        } else if ("willShowSurveyHandler".equals(action)) {
            willShowSurveyHandler(callbackContext);

        } else if ("identifyUserWithEmail".equals(action)) {
            identifyUserWithEmail(callbackContext, args.optString(0), args.optString(1));

        } else if ("logOut".equals(action)) {
            logOut(callbackContext);

        } else if ("getAllUserAttributes".equals(action)) {
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

        } else if ("setInvocationOptions".equals(action)) {
            setInvocationOptions(callbackContext, args.optJSONArray(0));

        } else if ("showSurveyIfAvailable".equals(action)) {
            showSurveyIfAvailable(callbackContext);

        } else if ("getAvailableSurveys".equals(action)) {
            getAvailableSurveys(callbackContext);

        } else if ("setAutoShowingSurveysEnabled".equals(action)) {
            setAutoShowingSurveysEnabled(callbackContext, args.optBoolean(0));

        } else if ("setShakingThreshold".equals(action)) {
            setShakingThreshold(callbackContext, args.optInt(0));

        } else if ("setEmailFieldRequiredForFeatureRequests".equals(action)) {
            setEmailFieldRequiredForFeatureRequests(callbackContext, args.optBoolean(0), args.optJSONArray(1));
        } else if ("showWelcomeMessage".equals(action)) {
            showWelcomeMessage(callbackContext, args.optString(0));
        } else if ("show".equals(action)) {
            show(callbackContext);
        } else if ("setReportTypes".equals(action)) {
            setReportTypes(callbackContext, args.optJSONArray(0));
        } else if ("showBugReportingWithReportTypeAndOptions".equals(action)) {
            showBugReportingWithReportTypeAndOptions(callbackContext, args.getString(0), args.optJSONArray(1));
        } else if ("setBugReportingEnabled".equals(action)) {
            setBugReportingEnabled(callbackContext, args.getBoolean(0));
        } else if ("setChatsEnabled".equals(action)) {
            setChatsEnabled(callbackContext, args.getBoolean(0));
        } else if ("setRepliesEnabled".equals(action)) {
            setRepliesEnabled(callbackContext, args.getBoolean(0));
        } else if ("showChats".equals(action)) {
            showChats(callbackContext);
        } else if ("showReplies".equals(action)) {
            showReplies(callbackContext);
        } else if ("hasChats".equals(action)) {
            hasChats(callbackContext);
        } else if ("getUnreadRepliesCount".equals(action)) {
            getUnreadRepliesCount(callbackContext);
        } else {
                // Method not found.
                return false;
        }
        return true;
    }

    private final void show(final CallbackContext callbackContext) {
        try {
            Instabug.show();
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    private void getUnreadRepliesCount(CallbackContext callbackContext) {
        callbackContext.success(Replies.getUnreadRepliesCount());
    }

    private final void setBugReportingEnabled(CallbackContext callbackContext, boolean isEnabled) {
        if (isEnabled) {
            BugReporting.setState(Feature.State.ENABLED);
        } else {
            BugReporting.setState(Feature.State.DISABLED);
        }
        callbackContext.success();
    }

    private final void setChatsEnabled(CallbackContext callbackContext, boolean isEnabled) {
        if (isEnabled) {
            Chats.setState(Feature.State.ENABLED);
        } else {
            Chats.setState(Feature.State.DISABLED);
        }
        callbackContext.success();
    }

    private final void setRepliesEnabled(CallbackContext callbackContext, boolean isEnabled) {
        if (isEnabled) {
            Replies.setState(Feature.State.ENABLED);
        } else {
            Replies.setState(Feature.State.DISABLED);
        }
        callbackContext.success();
    }

    private final void showChats(CallbackContext callbackContext) {
        Chats.show();
        callbackContext.success();
    }

    private final void showReplies(CallbackContext callbackContext) {
        Replies.show();
        callbackContext.success();
    }

    private final void hasChats(CallbackContext callbackContext) {
        if (Replies.hasChats()) {
            callbackContext.success();
        }
    }

    private final void setReportTypes(final CallbackContext callbackContext, final JSONArray types) {
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
    
    private final void showBugReportingWithReportTypeAndOptions(CallbackContext callbackContext, String reportType, JSONArray options) {
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
    private void activate(final CallbackContext callbackContext, JSONArray args) {
        try {
            activationIntent.putExtra("token", args.optJSONObject(0).getString("token"));
        } catch (JSONException e) {
            callbackContext.error("An application token must be provided.");
            return;
        }

        try {
            activationIntent.putExtra("invocation", args.getString(1));
        } catch (JSONException e) {
            callbackContext.error("An invocation event must be provided.");
            return;
        }

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
     * @param options the invocation options array
     */
    public void setInvocationOptions(final CallbackContext callbackContext, JSONArray options) {
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
     * @param isRequired A boolean to indicate whether email field is required or not.
     *
     * @param actionTypes Action types values
     */
    public void setEmailFieldRequiredForFeatureRequests(final CallbackContext callbackContext, boolean isRequired, JSONArray actionTypes) {
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
    private void setPreInvocationHandler(final CallbackContext callbackContext) {
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
    private void setPreSendingHandler(final CallbackContext callbackContext) {
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
    private void willShowSurveyHandler(final CallbackContext callbackContext) {
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
    private void didDismissSurveyHandler(final CallbackContext callbackContext) {
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
    private void setPostInvocationHandler(final CallbackContext callbackContext) {
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
    private void getAvailableSurveys(final CallbackContext callbackContext) {
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
     * @param colorString     The value of the primary color
     */
    private void setPrimaryColor(final CallbackContext callbackContext, final String colorString) {

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
                callbackContext.error(errorMsg);
            }

        } else
            callbackContext.error("A colorInt must be provided.");
    }

    /**
     * Sets the threshold value of the shake gesture on the device
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     * @param shakingThreshold
     *        The value of the primary color
     */
    private void setShakingThreshold(final CallbackContext callbackContext, int shakingThreshold) {
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
     * @param email           User's default email
     * @param name            Username
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
        } else
            callbackContext.error("A name and email must be provided.");
    }

    /**
     * Logout User
     *
     * @param callbackContext Used when calling back into JavaScript
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
     * @param callbackContext Used when calling back into JavaScript
     * @param data            String representing user data.
     */
    private void setUserData(final CallbackContext callbackContext, String data) {
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
     * @param surveyToken - A String with a survey token.
     *
     */
    private void showSurveyWithToken(final CallbackContext callbackContext, String surveyToken) {
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
     * @param log             Log message
     */
    private void addLog(final CallbackContext callbackContext, String log) {
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
    private void clearLog(final CallbackContext callbackContext) {
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
     * @param events
     *        Event to be used to invoke SDK.
     */
    private void setInvocationEvents(final CallbackContext callbackContext, JSONArray events) {
        String[] stringArrayOfEvents = toStringArray(events);
        ArrayList<InstabugInvocationEvent> invocationEvents = new ArrayList<InstabugInvocationEvent>();
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
                BugReporting.setInvocationEvents(invocationEvents.toArray(new InstabugInvocationEvent[0]));
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
     * @param callbackContext Used when calling back into JavaScript
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
     * @param callbackContext Used when calling back into JavaScript
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
     * @param isDebugEnabled  whether debug logs should be printed or not into
     *                        LogCat
     *
     * @param callbackContext Used when calling back into JavaScript
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
     * Sets whether auto surveys showing are enabled or not.
     *
     * @param isEnabled whether debug logs should be printed or not into LogCat
     *
     * @param callbackContext
     *        Used when calling back into JavaScript
     */
    private void setAutoShowingSurveysEnabled(final CallbackContext callbackContext, boolean isEnabled) {
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
    private void setChatNotificationEnabled(final CallbackContext callbackContext, boolean isEnabled) {
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
     * @param isEnabled       whether view hierarchy should be enabled or not
     *
     * @param callbackContext Used when calling back into JavaScript
     */
    private void setViewHierarchyEnabled(final CallbackContext callbackContext, boolean isEnabled) {
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
    private void showSurveyIfAvailable(final CallbackContext callbackContext) {
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
     * @param duration        maximum duration of the screen recording video seconds
     *                        The maximum duration is 30 seconds
     *
     * @param callbackContext Used when calling back into JavaScript
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
     * Sets user attribute to overwrite it's value or create a new one if it doesn't
     * exist.
     *
     * @param callbackContext Used when calling back into JavaScript
     * @param key             the attribute
     * @param value           the value
     *
     */
    private void setUserAttribute(final CallbackContext callbackContext, String key, String value) {
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
     * @param key             the attribute key as string
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
     * @param callbackContext Used when calling back into JavaScript
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
     * @param callbackContext Used when calling back into JavaScript
     * @param key             the attribute key as string
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
     * Returns true if the survey with a specific token was answered before. Will
     * return false if the token does not exist or if the survey was not answered
     * before.
     * 
     * @param surveyToken     - A String with a survey token.
     * @param callbackContext callback with argument as the desired value of the
     *                        whether the survey has been responded to or not.
     *
     */
    private void hasRespondedToSurveyWithToken(final CallbackContext callbackContext, String surveyToken) {
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
     * @param corner          the attribute position as string
     *
     */
    private void setVideoRecordingFloatingButtonPosition(final CallbackContext callbackContext, String corner) {
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
     * @param mode            A string to disable the extended bug report mode,
     *                        enable it with required or with optional fields
     */
    private void setExtendedBugReportMode(final CallbackContext callbackContext, String mode) {
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
     * @param reproStepsMode  A string to set user steps tracking to be enabled, non
     *                        visual or disabled.
     */
    private void setReproStepsMode(final CallbackContext callbackContext, String reproStepsMode) {
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
     * @param welcomeMessageMode A string to set user steps tracking to be enabled,
     *                           non visual or disabled.
     */
    private void showWelcomeMessage(final CallbackContext callbackContext, String welcomeMessageMode) {
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
     * @param sessionsCount   number of sessions that the dismissed survey will be
     *                        shown after.
     * @param daysCount       number of days that the dismissed survey will show
     *                        after.
     */
    private void setThresholdForReshowingSurveyAfterDismiss(final CallbackContext callbackContext, int sessionsCount,
            int daysCount) {
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
    private void showFeatureRequests(final CallbackContext callbackContext) {
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
     * @param shouldShow      whether should show welcome screen Surveys before
     *                        surveys or not
     *
     * @param callbackContext Used when calling back into JavaScript
     */
    private void setShouldShowSurveysWelcomeScreen(final CallbackContext callbackContext, boolean shouldShow) {
        try {
            Surveys.setShouldShowWelcomeScreen(shouldShow);
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
