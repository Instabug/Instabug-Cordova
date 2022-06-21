var exec = require('cordova/exec');
var registry = require("./ArgsRegistry");

var getInvocationEvents = function () {
    return {
        shake: 'shake',
        button: 'button',
        screenshot: 'screenshot',
        swipe: 'swipe',
        none: 'none'
    };
};

var getReproStepsMode = function () {
    return {
        enabled: 'enabled',
        disabled: 'disabled',
        enabledWithNoScreenshots: 'enabledWithNoScreenshots'
    };
};

var getWelcomeMessageMode = function () {
    return {
        welcomeMessageModeLive: 'welcomeMessageModeLive',
        welcomeMessageModeBeta: 'welcomeMessageModeBeta',
        welcomeMessageModeDisabled: 'welcomeMessageModeDisabled'
    }
};

var getLocales = function () {
    return {
        arabic: 'arabic',
        azerbaijani: 'azerbaijani',
        chineseSimplified: 'chineseSimplified',
        chineseTraditional: 'chineseTraditional',
        danish: 'danish',
        dutch: 'dutch',
        english: 'english',
        french: 'french',
        german: 'german',
        italian: 'italian',
        japanese: 'japanese',
        korean: 'korean',
        polish: 'polish',
        portugueseBrazil: 'portugueseBrazil',
        russian: 'russian',
        spanish: 'spanish',
        swedish: 'swedish',
        turkish: 'turkish',
        czech: 'czech'
    };
};

var Instabug = function () {
};

Instabug.welcomeMessageMode = registry.welcomeMessageMode;
Instabug.floatingButtonEdge = registry.floatingButtonEdge;
Instabug.colorTheme = registry.colorTheme;
Instabug.strings = registry.strings;

/**
 * Starts the SDK.
 * This is the main SDK method that does all the magic. This is the only
 * method that SHOULD be called.
 * Should be called in constructor of the AppRegistry component
 * 
 * @param {string} token The token that identifies the app, you can find
 * it on your dashboard.
 * @param {keyof BugReporting.option} invocationEvents events that invokes
 * the SDK's UI.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.start = function (token, invocationEvents, success, error) {
    var validEvents = getInvocationEvents();
    var isValid = invocationEvents.every((e) => validEvents[e]);

    if (isValid && invocationEvents.length > 0) {
        exec(success, error, 'IBGPlugin', 'start', [token, invocationEvents]);
    } else {
        console.log('Could not start Instabug -  invalid invocation events');
    }
};

/**
 * Shows default Instabug prompt.
 * 
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.show = function (success, error) {
    exec(success, error, 'IBGPlugin', 'show');
}

/**
 * Sets the primary color of the SDK's UI.
 * Sets the color of UI elements indicating interactivity or call to action.
 * To use, import processColor and pass to it with argument the color hex
 * as argument.
 * 
 * @param {color} color A color to set the UI elements of the SDK to.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.setPrimaryColor = function (color, success, error) {
    exec(success, error, 'IBGPlugin', 'setPrimaryColor', [colorInteger]);
};

/**
 * Logs a user event that happens through the lifecycle of the application.
 * Logged user events are going to be sent with each report, as well as at the end of a session.
 * 
 * @param {string} userEvent Event name.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.logUserEventWithName = function (userEvent, success, error) {
    exec(success, error, 'IBGPlugin', 'logUserEventWithName', [userEvent]);
};

/**
 * Sets whether user steps tracking is visual, non visual or disabled.
 * User Steps tracking is enabled by default if it's available
 * in your current plan.
 *
 * @param {reproStepsMode} reproStepsMode An enum to set user steps tracking
 * to be enabled, non visual or disabled.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.setReproStepsMode = function (reproStepsMode, success, error) {

  var validatedReproStepsMode = getReproStepsMode()[reproStepsMode];

  if (validatedReproStepsMode) {
      exec(success, error, 'IBGPlugin', 'setReproStepsMode', [validatedReproStepsMode]);
  } else {
      console.log('Could not set user steps mode - "' + validatedReproStepsMode + '" is not valid.');
  }
};

/**
 * The session profiler is enabled by default and it attaches to the bug and
 * crash reports the following information during the last 60 seconds before the report is sent.
 * @param {boolean} isEnabled - A boolean parameter to enable or disable the feature.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
 Instabug.setSessionProfilerEnabled = function (isEnabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setSessionProfilerEnabled', [isEnabled]);
};

/**
 * Sets whether the SDK is tracking user steps or not.
 * Enabling user steps would give you an insight on the scenario a user has
 * performed before encountering a bug or a crash. User steps are attached
 * with each report being sent.
 * @param {boolean} isEnabled A boolean to set user steps tracking
 * to being enabled or disabled.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.setTrackUserStepsEnabled = function (isEnabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setTrackUserStepsEnabled', [isEnabled]);
};

/**
 * Sets the welcome message mode to live, beta or disabled.
 * @param {keyof Instabug.welcomeMessageMode} mode.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.setWelcomeMessageMode = function (mode, success, error) {
    exec(success, error, 'IBGPlugin', 'setWelcomeMessageMode', [mode]);
};

/**
 * Shows the welcome message in a specific mode.
 * @param {keyof Instabug.welcomeMessageMode} mode.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.showWelcomeMessage = function (mode, success, error) {
    exec(success, error, 'IBGPlugin', 'showWelcomeMessage', [mode]);
};

/**
 * Attaches user data to each report being sent.
 * Each call to this method overrides the user data to be attached.
 * Maximum size of the string is 1,000 characters.
 * 
 * @param {string} data A string to be attached to each report, with a
 * maximum size of 1,000 characters.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.setUserData = function (data, success, error) {
    exec(success, error, 'IBGPlugin', 'setUserData', [data]);
};

/**
 * Add file to be attached to the bug report.
 * 
 * @param {string} filePath
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.addFile = function (filePath, success, error) {
    exec(success, error, 'IBGPlugin', 'addFile', [filePath]);
}

/**
 * Appends a log message to Instabug internal log
 * <p>
 * These logs are then sent along the next uploaded report.
 * All log messages are timestamped <br/>
 * Logs aren't cleared per single application run.
 * If you wish to reset the logs,
 * use {@link #clearLogs()} ()}
 * </p>
 * Note: logs passed to this method are <b>NOT</b> printed to Logcat
 *
 * @param message the message
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.addLog = function (content, success, error) {
    exec(success, error, 'IBGPlugin', 'addLog', [content]);
};

/**
 * Clear all Instabug logs, console logs, network logs and user steps.
 * 
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.clearLog = function (success, error) {
    exec(success, error, 'IBGPlugin', 'clearLog', []);
};

/**
 * Sets whether IBGLog should also print to Xcode's console log or not.
 * 
 * @param {boolean} isEnabled A boolean to set whether printing to
 *                  Xcode's console is enabled or not.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.setIBGLogPrintsToConsole = function (isEnabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setIBGLogPrintsToConsole', [isEnabled]);
};

/**
 * Disables all Instabug functionality
 * It works on android only
 * 
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.disable = function (success, error) {
    exec(success, error, 'IBGPlugin', 'disable', []);
};

/**
 * Enables all Instabug functionality
 * It works on android only
 * 
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.enable = function (success, error) {
    exec(success, error, 'IBGPlugin', 'enable', []);
};

Instabug.isEnabled = function (success, error) {
    exec(success, error, 'IBGPlugin', 'isEnabled', []);
};

/**
 * Sets user attribute to overwrite it's value or create a new one if it doesn't exist.
 *
 * @param key   the attribute
 * @param value the value
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.setUserAttribute = function (key, value, success, error) {
    exec(success, error, 'IBGPlugin', 'setUserAttribute', [key, value]);
};

/**
 * Removes user attribute if exists.
 *
 * @param key the attribute key as string
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.removeUserAttribute = function (key, success, error) {
    exec(success, error, 'IBGPlugin', 'removeUserAttribute', [key]);
};

/**
 * Returns all user attributes.
 * 
 * @param {function} userAttributesCallback callback with argument A new dictionary containing
 * all the currently set user attributes, or an empty dictionary if no user attributes have been set.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.getAllUserAttributes = function (success, error) {
    exec(success, error, 'IBGPlugin', 'getAllUserAttributes', []);
};

/**
 * Returns the user attribute associated with a given key.
 * 
 * @param {string} key The attribute key as string
 * @param {function} userAttributeCallback callback with argument as the desired user attribute value
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.getUserAttribute = function (key, success, error) {
    exec(success, error, 'IBGPlugin', 'getUserAttribute', [key]);
};

/**
 * Sets the default value of the user's email and hides the email field from the reporting UI
 * and set the user's name to be included with all reports.
 * It also reset the chats on device to that email and removes user attributes,
 * user data and completed surveys.
 * 
 * @param {string} email Email address to be set as the user's email.
 * @param {string} name Name of the user to be set.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.identifyUserWithEmail = function (email, name, success, error) {
    exec(success, error, 'IBGPlugin', 'identifyUserWithEmail', [email, name]);
};

Instabug.setPreSendingHandler = function (success, error) {
    exec(success, error, 'IBGPlugin', 'setPreSendingHandler', []);
};

/**
 * Sets the default value of the user's email to nil and show email field and remove user name
 * from all reports
 * It also reset the chats on device and removes user attributes, user data and completed surveys.
 * 
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.logOut = function (success, error) {
    exec(success, error, 'IBGPlugin', 'logOut', []);
};

/**
 * Enable/Disable debug logs from Instabug SDK
 * Default state: disabled
 *
 * @param {boolean} isDebugEnabled whether debug logs should be printed or not into LogCat
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.setDebugEnabled = function (isDebugEnabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setDebugEnabled', [isDebugEnabled]);
    if(success) {
        console.log("setting debug enabled to " + isDebugEnabled);
    } else if(error) {
        console.log("setting debug enabled not successful");
    }
};

/**
 * Sets the SDK's locale.
 * Use to change the SDK's UI to different language.
 * Defaults to the device's current locale.
 * 
 * @param {locale} locale A locale to set the SDK to.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.setLocale = function (locale, success, error) {
    var validatedLocale = getLocales()[locale];

    if (validatedLocale) {
        exec(success, error, 'IBGPlugin', 'setLocale', [validatedLocale]);
    } else {
        console.log('Could not set locale - "' + locale + '" is not valid.');
    }
};

/**
 * Sets SDK color theme.
 * 
 * @param {keyof Instabug.colorTheme} theme.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.setColorTheme = function (theme, success, error) {
    exec(success, error, 'IBGPlugin', 'setColorTheme', [theme]);
};

/**
 * Overrides any of the strings shown in the SDK with custom ones.
 * Allows you to customize any of the strings shown to users in the SDK.
 * @param {strings} key Key of string to override.
 * @param {string} value String value to override the default one.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
Instabug.setString = function (key, value, success, error) {
    exec(success, error, 'IBGPlugin', 'setString', [key, value]);
};

module.exports = Instabug;
