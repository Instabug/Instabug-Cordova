var exec = require('cordova/exec');

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

Instabug.start = function (token, invocationEvents, success, error) {
    const validEvents = getInvocationEvents();
    const isValid = invocationEvents.every((e) => validEvents[e]);

    if (isValid && invocationEvents.length > 0) {
        exec(success, error, 'IBGPlugin', 'start', [token, invocationEvents]);
    } else {
        console.log('Could not start Instabug -  invalid invocation events');
    }
};

Instabug.show = function (success, error) {
    exec(success, error, 'IBGPlugin', 'show');
}

Instabug.setPrimaryColor = function (colorInteger, success, error) {
    exec(success, error, 'IBGPlugin', 'setPrimaryColor', [colorInteger]);
};

Instabug.setViewHierarchyEnabled = function (enabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setViewHierarchyEnabled', [enabled]);
};

Instabug.setAutoScreenRecordingMaxDuration = function (duration, success, error) {
    exec(success, error, 'IBGPlugin', 'setAutoScreenRecordingMaxDuration', [duration]);
};

Instabug.logUserEventWithName = function (userEvent, success, error) {
    exec(success, error, 'IBGPlugin', 'logUserEventWithName', [userEvent]);
};

Instabug.setShakingThreshold = function (shakingThreshold, success, error) {
    exec(success, error, 'IBGPlugin', 'setShakingThreshold', [shakingThreshold]);
};

Instabug.setReproStepsMode = function (reproStepsMode, success, error) {

  var validatedReproStepsMode = getReproStepsMode()[reproStepsMode];

  if (validatedReproStepsMode) {
      exec(success, error, 'IBGPlugin', 'setReproStepsMode', [validatedReproStepsMode]);
  } else {
      console.log('Could not set user steps mode - "' + validatedReproStepsMode + '" is not valid.');
  }
};

Instabug.showWelcomeMessage = function (welcomeMessageMode, success, error) {

    var validatedWelcomeMessageMode = getWelcomeMessageMode()[welcomeMessageMode];
  
    if (validatedWelcomeMessageMode) {
        exec(success, error, 'IBGPlugin', 'showWelcomeMessage', [validatedWelcomeMessageMode]);
    } else {
        console.log('Could not set welcome message mode - "' + validatedWelcomeMessageMode + '" is not valid.');
    }
  };

Instabug.setUserData = function (data, success, error) {
    exec(success, error, 'IBGPlugin', 'setUserData', [data]);
};

Instabug.addFile = function (filePath, success, error) {
    exec(success, error, 'IBGPlugin', 'addFile', [filePath]);
}

Instabug.addLog = function (content, success, error) {
    exec(success, error, 'IBGPlugin', 'addLog', [content]);
};

Instabug.clearLog = function (success, error) {
    exec(success, error, 'IBGPlugin', 'clearLog', []);
};

/**
 * @deprecated use {@link Replies.getUnreadRepliesCount}
 */
Instabug.getUnreadMessagesCount = function (success, error) {
    exec(success, error, 'IBGPlugin', 'getUnreadRepliesCount', []);
};

/**
 * @deprecated use {@link Replies.setInAppNotificationEnabled}
 */
Instabug.setChatNotificationEnabled = function (isEnabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setChatNotificationEnabled', [isEnabled]);
};

Instabug.setIBGLogPrintsToConsole = function (isEnabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setIBGLogPrintsToConsole', [isEnabled]);
};

Instabug.disable = function (success, error) {
    exec(success, error, 'IBGPlugin', 'disable', []);
};

Instabug.enable = function (success, error) {
    exec(success, error, 'IBGPlugin', 'enable', []);
};

Instabug.isEnabled = function (success, error) {
    exec(success, error, 'IBGPlugin', 'isEnabled', []);
};

Instabug.setUserAttribute = function (key, value, success, error) {
    exec(success, error, 'IBGPlugin', 'setUserAttribute', [key, value]);
};

Instabug.removeUserAttribute = function (key, success, error) {
    exec(success, error, 'IBGPlugin', 'removeUserAttribute', [key]);
};

Instabug.getAllUserAttributes = function (success, error) {
    exec(success, error, 'IBGPlugin', 'getAllUserAttributes', []);
};

Instabug.getUserAttribute = function (key, success, error) {
    exec(success, error, 'IBGPlugin', 'getUserAttribute', [key]);
};

Instabug.identifyUserWithEmail = function (email, name, success, error) {
    exec(success, error, 'IBGPlugin', 'identifyUserWithEmail', [email, name]);
};

Instabug.setPreSendingHandler = function (success, error) {
    exec(success, error, 'IBGPlugin', 'setPreSendingHandler', []);
};

Instabug.setVideoRecordingFloatingButtonPosition = function (position, success, error) {
    exec(success, error, 'IBGPlugin', 'setVideoRecordingFloatingButtonPosition', [position]);
};

Instabug.logOut = function (success, error) {
    exec(success, error, 'IBGPlugin', 'logOut', []);
};

Instabug.setDebugEnabled = function (isDebugEnabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setDebugEnabled', [isDebugEnabled]);
    if(success) {
        console.log("setting debug enabled to " + isDebugEnabled);
    } else if(error) {
        console.log("setting debug enabled not successful");
    }
};

Instabug.setLocale = function (locale, success, error) {
    var validatedLocale = getLocales()[locale];

    if (validatedLocale) {
        exec(success, error, 'IBGPlugin', 'setLocale', [validatedLocale]);
    } else {
        console.log('Could not set locale - "' + locale + '" is not valid.');
    }
};

module.exports = Instabug;
