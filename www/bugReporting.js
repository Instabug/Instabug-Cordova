/*
 * Instabug Bug Reporting module.
 * @file bugReporting.js
 */

var exec = require('cordova/exec');

/**
 * The event used to invoke the feedback form
 * @readonly
 * @enum {string} InvocationEvent
 */
var getInvocationEvents = function() {
  return {
    shake: 'shake',
    button: 'button',
    screenshot: 'screenshot',
    swipe: 'swipe',
    none: 'none'
  };
};

/**
 * The options used upon invoking the SDK
 * @readonly
 * @enum {string} InvocationOption
 */
var getInvocationOptions = function() {
  return {
    emailFieldHidden: 'emailFieldHidden',
    emailFieldOptional: 'emailFieldOptional',
    commentFieldRequired: 'commentFieldRequired',
    disablePostSendingDialog: 'disablePostSendingDialog'
  };
};

/**
 * The mode used upon invocating the SDK
 * @readonly
 * @enum {string} InvocationMode
 */
var getInvocationModes = function() {
  return {
    chat: 'chat',
    chats: 'chats',
    bug: 'bug',
    feedback: 'feedback',
    options: 'options'
  };
};

/**
 * The prompt options available in the SDK
 * @readonly
 * @enum {string} PromptOption
 */
var getPromptOptions = function() {
  return {
    bug: 'bug',
    chat: 'chat',
    feedback: 'feedback'
  };
};

/**
 * The extended bug report mode.
 * @readonly
 * @enum {string} ExtendedBugReportMode
 */
var getExtendedBugReportMode = function() {
  return {
    enabledWithRequiredFields: 'enabledWithRequiredFields',
    enabledWithOptionalFields: 'enabledWithOptionalFields',
    disabled: 'disabled'
  };
};

/**
 * BugReporting module
 * @exports BugReporting
 */
var BugReporting = function() {};

BugReporting.invocationEvents = getInvocationEvents();
BugReporting.invocationOptions = getInvocationOptions();
BugReporting.invocationModes = getInvocationModes();
BugReporting.promptOptions = getPromptOptions();
BugReporting.extendedBugReportMode = getExtendedBugReportMode();

/**
 * Sets the invocation options.
 * Default is set by `Instabug.startWithToken`.
 * @param {enum} invocationOptions Array of InvocationOption
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
BugReporting.setInvocationOptions = function(options, success, error) {
  var i;
  var validatedOptions = [];
  for (i = 0; i < options.length; i++) {
    var validatedOption = getInvocationOptions()[options[i]];
    if (validatedOption) {
      validatedOptions.push(validatedOption);
    }
  }
  if (validatedOptions !== undefined || validatedOptions.length != 0) {
    exec(success, error, 'IBGPlugin', 'setInvocationOptions', [validatedOptions]);
  } else {
    console.log(
      'Could not change invocation option - "' + validatedOptions + '" is empty.'
    );
  }
};

/**
 * Invokes the SDK manually with the default invocation mode.
 * Shows a view that asks the user whether they want to start a chat, report
 * a problem or suggest an improvement.
 * @param {enum} mode InvocationMode
 * @param {enum} invocationOptions Array of InvocationOption
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
BugReporting.invoke = function(mode, invocationOptions, success, error) {
  var validatedMode = getInvocationModes()[mode];
  var i;
  var validatedOptions = [];
  for (i = 0; i < invocationOptions.length; i++) {
    var validatedOption = getInvocationOptions()[invocationOptions[i]];
    if (validatedOption) {
      validatedOptions.push(validatedOption);
    }
  }
  if (validatedMode) {
    if (validatedOptions.length != 0) {
      exec(success, error, 'IBGPlugin', 'invoke', [validatedMode, validatedOptions]);
    } else {
      exec(success, error, 'IBGPlugin', 'invoke', [validatedMode]);
    }
  } else {
    exec(success, error, 'IBGPlugin', 'invoke', []);
    console.log('Could not apply mode to invocation - "' + mode + '" is not valid.');
  }
};

/**
 * Sets a block of code to be executed just before the SDK's UI is presented.
 * This block is executed on the UI thread. Could be used for performing any
 * UI changes before the SDK's UI is shown.
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
BugReporting.setOnInvokeHandler = function(success, error) {
  exec(success, error, 'IBGPlugin', 'setPreInvocationHandler', []);
};

/**
 * Sets a block of code to be executed right after the SDK's UI is dismissed.
 * This block is executed on the UI thread. Could be used for performing any
 * UI changes after the SDK's UI is dismissed.
 * @param {function(Object):void} success callback on function success; param includes dismissType and reportType
 * @param {function(void):void} error callback on function error
 */
BugReporting.setOnDismissHandler = function(success, error) {
  exec(success, error, 'IBGPlugin', 'setPostInvocationHandler', []);
};

/**
 * Sets a block of code to be executed when a prompt option is selected.
 * @param {function(string):void} success callback on function success;param includes promptOption
 * @param {function(void):void} error callback on function error
 */
BugReporting.setDidSelectPromptOptionHandler = function(success, error) {
  exec(success, error, 'IBGPlugin', 'didSelectPromptOptionHandler', []);
};

/**
 * Sets the events that will invoke the SDK.
 * @param {enum} events Array of InvocationEvent
 * @param {function(void):void} success callback on function success
 * @param {function(string):void} error callback on function error
 */
BugReporting.setInvocationEvents = function(events, success, error) {
  var i;
  var validatedEvents = [];
  for (i = 0; i < events.length; i++) {
    var validatedEvent = getInvocationEvents()[events[i]];
    if (validatedEvent) {
      validatedEvents.push(validatedEvent);
    }
  }
  if (validatedEvents !== undefined || validatedEvents.length != 0) {
    exec(success, error, 'IBGPlugin', 'setInvocationEvents', [validatedEvents]);
  } else {
    console.log('Could not change invocation event - "' + event + '" is not valid.');
  }
};

/**
 * Sets enabled types of attachments for bug reporting.
 * @param {boolean} screenshot
 * @param {boolean} extraScreenshot
 * @param {boolean} galleryImage
 * @param {boolean} screenRecording
 * @param {function(void):void} success callback on function success
 * @param {function(string):void} error callback on function error
 */
BugReporting.setEnabledAttachmentTypes = function(
  screenshot,
  extraScreenshot,
  galleryImage,
  screenRecording,
  success,
  error
) {
  exec(success, error, 'IBGPlugin', 'setAttachmentTypesEnabled', [
    screenshot,
    extraScreenshot,
    galleryImage,
    screenRecording
  ]);
};

/**
 * Sets the prompt options that will be shown to a user when the SDK is invoked.
 * @param {enum} promptOptions Array of PromptOption
 * @param {function(void):void} success callback on function success
 * @param {function(string):void} error callback on function error
 */
BugReporting.setPromptOptions = function(promptOptions, success, error) {
  var i;
  var validatedPromptOptions = [];
  for (i = 0; i < promptOptions.length; i++) {
    var validatedPromptOption = getPromptOptions()[promptOptions[i]];
    if (validatedPromptOption) {
      validatedPromptOptions.push(validatedPromptOption);
    }
  }
  if (validatedPromptOptions !== undefined || validatedPromptOptions.length != 0) {
    exec(success, error, 'IBGPlugin', 'setPromptOptionsEnabled', [
      validatedPromptOptions
    ]);
  } else {
    console.log(
      'Could not change prompt option - "' + validatedPromptOption + '" is not valid.'
    );
  }
};

/**
 * 
 * @param {enum} extendedBugReportMode ExtendedBugReportMode
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error 
 */
BugReporting.setExtendedBugReportMode = function(extendedBugReportMode, success, error) {
  var validatedExtendedBugReportMode = getExtendedBugReportMode()[extendedBugReportMode];

  if (validatedExtendedBugReportMode) {
    exec(success, error, 'IBGPlugin', 'setExtendedBugReportMode', [
      validatedExtendedBugReportMode
    ]);
  } else {
    console.log(
      'Could not set extended bug report mode - "' +
        validatedExtendedBugReportMode +
        '" is not valid.'
    );
  }
};

module.exports = BugReporting;
