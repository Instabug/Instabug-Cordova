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

var getInvocationModes = function () {
    return {
        chat: 'chat',
        chats: 'chats',
        bug: 'bug',
        feedback: 'feedback',
        options: 'options'
    };
};

var getInvocationOptions = function () {
    return {
        emailFieldHidden: 'emailFieldHidden',
        emailFieldOptional: 'emailFieldOptional',
        commentFieldRequired: 'commentFieldRequired',
        disablePostSendingDialog: 'disablePostSendingDialog'
    };
};

var getReproStepsMode = function () {
    return {
        enabled: 'enabled',
        disabled: 'disabled',
        enabledWithNoScreenshots: 'enabledWithNoScreenshots'
    };
};

var getPromptOptions = function () {
    return {
        bug: 'bug',
        chat: 'chat',
        feedback: 'feedback'
    }
};
var getWelcomeMessageMode = function () {
    return {
        welcomeMessageModeLive: 'welcomeMessageModeLive',
        welcomeMessageModeBeta: 'welcomeMessageModeBeta',
        welcomeMessageModeDisabled: 'welcomeMessageModeDisabled'
    }
    
};

var getExtendedBugReportMode = function () {
    return {
        enabledWithRequiredFields: 'enabledWithRequiredFields',
        enabledWithOptionalFields: 'enabledWithOptionalFields',
        disabled: 'disabled'
    };
};

var getActionTypes = function () {
    return {
        requestNewFeature: 'requestNewFeature',
        addCommentToFeature: 'addCommentToFeature'
    };
};

var getLocales = function () {
    return {
        arabic: 'arabic',
        chineseSimplified: 'chineseSimplified',
        chineseTraditional: 'chineseTraditional',
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

Instabug.activate = function (token, event, options, success, error) {
    var validatedEvent = getInvocationEvents()[event];

    if (validatedEvent) {
        console.warn("This method is now deprecated, and will be removed in an upcoming version.")
        exec(success, error, 'IBGPlugin', 'activate', [token, event, options]);
    } else {
        console.log('Could not activate Instabug - invocation event "' + event + '" is not valid.');
    }
};

Instabug.startWithToken = function (token, events, options, success, error) {
  var i;
  var validatedEvents = [];
  for (i = 0; i < events.length; i++) {
    var validatedEvent = getInvocationEvents()[events[i]];
    if(validatedEvent) {
      validatedEvents.push(validatedEvent);
    }
  }
  if (validatedEvents !== undefined || validatedEvents.length != 0) {
    exec(success, error, 'IBGPlugin', 'startWithToken', [token, validatedEvents, options]);
  } else {
      console.log('Could not activate Instabug - invocation event is not valid.');
  }
};

Instabug.invoke = function (mode, invocationOptions, success, error) {
    var validatedMode = getInvocationModes()[mode];
    var i;
    var validatedOptions = [];
    for (i = 0; i < invocationOptions.length; i++) {
      var validatedOption = getInvocationOptions()[invocationOptions[i]];
      if(validatedOption) {
        validatedOptions.push(validatedOption);
      }
    }
    if (validatedMode) {
        if(validatedOptions.length != 0) {
            exec(success, error, 'IBGPlugin', 'invoke', [validatedMode, validatedOptions]);
        } else {
            exec(success, error, 'IBGPlugin', 'invoke', [validatedMode]);
        }
    } else {
        exec(success, error, 'IBGPlugin', 'invoke', []);
        console.log('Could not apply mode to invocation - "' + mode + '" is not valid.');
    }
};

Instabug.showIntro = function (success, error) {
    exec(success, error, 'IBGPlugin', 'showIntroDialog', []);
};

Instabug.setPrimaryColor = function (colorInteger, success, error) {
    exec(success, error, 'IBGPlugin', 'setPrimaryColor', [colorInteger]);
};

Instabug.setViewHierarchyEnabled = function (enabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setViewHierarchyEnabled', [enabled]);
};

Instabug.setAutoScreenRecordingEnabled = function (enabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setAutoScreenRecordingEnabled', [enabled]);
};

Instabug.setAutoScreenRecordingMaxDuration = function (duration, success, error) {
    exec(success, error, 'IBGPlugin', 'setAutoScreenRecordingMaxDuration', [duration]);
};

Instabug.setUserEmail = function (email, success, error) {
    exec(success, error, 'IBGPlugin', 'setUserEmail', [email]);
};

Instabug.setAttachmentTypesEnabled = function (screenshot, extraScreenshot, 
                                                galleryImage, screenRecording, 
                                                success, error) {
    exec(success, error, 'IBGPlugin', 'setAttachmentTypesEnabled', [screenshot,
        extraScreenshot, galleryImage, screenRecording]);
};

Instabug.setUserName = function (name, success, error) {
    exec(success, error, 'IBGPlugin', 'setUserName', [name]);
};

Instabug.showSurveyWithToken = function (surveyToken, success, error) {
    exec(success, error, 'IBGPlugin', 'showSurveyWithToken', [surveyToken]);
};

Instabug.logUserEventWithName = function (userEvent, success, error) {
    exec(success, error, 'IBGPlugin', 'logUserEventWithName', [userEvent]);
};

Instabug.setShakingThreshold = function (shakingThreshold, success, error) {
    exec(success, error, 'IBGPlugin', 'setShakingThreshold', [shakingThreshold]);
};

Instabug.setEmailFieldRequiredForFeatureRequests = function (isRequired, actionTypes, success, error) {
    var i;
    var validatedActionTypes = [];
    for (i = 0; i < actionTypes.length; i++) {
      var validatedActionType = getActionTypes()[actionTypes[i]];
      if(validatedActionType) {
        validatedActionTypes.push(validatedActionType);
      }
    }
    if (validatedActionTypes !== undefined || validatedActionTypes.length != 0) {
      exec(success, error, 'IBGPlugin', 'setEmailFieldRequiredForFeatureRequests', [isRequired, validatedActionTypes]);
    } else {
        console.log('Could not set email field - "' + validatedActionTypes + '" is incorrect.');
    }
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

Instabug.setExtendedBugReportMode = function (extendedBugReportMode, success, error) {

  var validatedExtendedBugReportMode = getExtendedBugReportMode()[extendedBugReportMode];

  if (validatedExtendedBugReportMode) {
      exec(success, error, 'IBGPlugin', 'setExtendedBugReportMode', [validatedExtendedBugReportMode]);
  } else {
      console.log('Could not set extended bug report mode - "' + validatedExtendedBugReportMode + '" is not valid.');
  }
};

Instabug.setPromptOptionsEnabled = function (promptOptions, success, error) {
    var i;
    var validatedPromptOptions = [];
    for (i = 0; i < promptOptions.length; i++) {
      var validatedPromptOption = getPromptOptions()[promptOptions[i]];
      if(validatedPromptOption) {
        validatedPromptOptions.push(validatedPromptOption);
      }
    }
    if (validatedPromptOptions !== undefined || validatedPromptOptions.length != 0) {
      exec(success, error, 'IBGPlugin', 'setPromptOptionsEnabled', [validatedPromptOptions]);
    } else {
        console.log('Could not change prompt option - "' + validatedPromptOption + '" is not valid.');
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

Instabug.getUnreadMessagesCount = function (success, error) {
    exec(success, error, 'IBGPlugin', 'getUnreadMessagesCount', []);
};

Instabug.setChatNotificationEnabled = function (isEnabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setChatNotificationEnabled', [isEnabled]);
};

Instabug.setIBGLogPrintsToConsole = function (isEnabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setIBGLogPrintsToConsole', [isEnabled]);
};

Instabug.setSurveysEnabled = function (isEnabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setSurveysEnabled', [isEnabled]);
};

Instabug.showSurveyIfAvailable = function (success, error) {
    exec(success, error, 'IBGPlugin', 'showSurveyIfAvailable', []);
};

/**
 * @deprecated Since version 8.0. 
 */
Instabug.changeInvocationEvent = function (event, success, error) {
    var validatedEvent = getInvocationEvents()[event];

    if (validatedEvent) {
        exec(success, error, 'IBGPlugin', 'changeInvocationEvent', [validatedEvent]);
    } else {
        console.log('Could not change invocation event - "' + event + '" is not valid.');
    }
};

Instabug.setInvocationEvents = function (events, success, error) {
  var i;
  var validatedEvents = [];
  for (i = 0; i < events.length; i++) {
    var validatedEvent = getInvocationEvents()[events[i]];
    if(validatedEvent) {
      validatedEvents.push(validatedEvent);
    }
  }
  if (validatedEvents !== undefined || validatedEvents.length != 0) {
    exec(success, error, 'IBGPlugin', 'setInvocationEvents', [validatedEvents]);
  } else {
      console.log('Could not change invocation event - "' + event + '" is not valid.');
  }
};

Instabug.setInvocationOptions = function (options, success, error) {
    var i;
    var validatedOptions = [];
    for (i = 0; i < options.length; i++) {
      var validatedOption = getInvocationOptions()[options[i]];
      if(validatedOption) {
        validatedOptions.push(validatedOption);
      }
    }
    if (validatedOptions !== undefined || validatedOptions.length != 0) {
      exec(success, error, 'IBGPlugin', 'setInvocationOptions', [validatedOptions]);
    } else {
        console.log('Could not change invocation option - "' + validatedOptions + '" is empty.');
    }
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

Instabug.hasRespondedToSurveyWithToken = function (surveyToken, success, error) {
    exec(success, error, 'IBGPlugin', 'hasRespondedToSurveyWithToken', [surveyToken]);
};

Instabug.getAvailableSurveys = function (success, error) {
    exec(success, error, 'IBGPlugin', 'getAvailableSurveys', []);
};

Instabug.identifyUserWithEmail = function (email, name, success, error) {
    exec(success, error, 'IBGPlugin', 'identifyUserWithEmail', [email, name]);
};

Instabug.setPreInvocationHandler = function (success, error) {
    exec(success, error, 'IBGPlugin', 'setPreInvocationHandler', []);
};

Instabug.setPostInvocationHandler = function (success, error) {
    exec(success, error, 'IBGPlugin', 'setPostInvocationHandler', []);
};

Instabug.setPreSendingHandler = function (success, error) {
    exec(success, error, 'IBGPlugin', 'setPreSendingHandler', []);
};

Instabug.setDidSelectPromptOptionHandler = function (success, error) {
    exec(success, error, 'IBGPlugin', 'didSelectPromptOptionHandler', []);
};

Instabug.setWillShowSurveyHandler = function (success, error) {
    exec(success, error, 'IBGPlugin', 'willShowSurveyHandler', []);
};

Instabug.setDidDismissSurveyHandler = function (success, error) {
    exec(success, error, 'IBGPlugin', 'didDismissSurveyHandler', []);
};

Instabug.setVideoRecordingFloatingButtonPosition = function (position, success, error) {
    exec(success, error, 'IBGPlugin', 'setVideoRecordingFloatingButtonPosition', [position]);
};

Instabug.logOut = function (success, error) {
    exec(success, error, 'IBGPlugin', 'logOut', []);
};

Instabug.dismiss = function (success, error) {
    exec(success, error, 'IBGPlugin', 'dismiss', []);
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

Instabug.setThresholdForReshowingSurveyAfterDismiss = function (sessionsCount, daysCount, success, error) {
    exec(success, error, 'IBGPlugin', 'setThresholdForReshowingSurveyAfterDismiss', [sessionsCount, daysCount]);
};

Instabug.setAutoShowingSurveysEnabled = function (autoShowingSurveysEnabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setAutoShowingSurveysEnabled', [autoShowingSurveysEnabled]);
};

Instabug.showFeatureRequests = function (success, error) {
    exec(success, error, 'IBGPlugin', 'showFeatureRequests', []);
};

Instabug.setShouldShowSurveysWelcomeScreen = function (shouldShowWelcomeScreen, success, error) {
    exec(success, error, 'IBGPlugin', 'setShouldShowSurveysWelcomeScreen', [shouldShowWelcomeScreen]);
};

module.exports = Instabug;
