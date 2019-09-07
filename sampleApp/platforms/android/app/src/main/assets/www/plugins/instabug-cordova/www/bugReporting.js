cordova.define("instabug-cordova.BugReporting", function(require, exports, module) {
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
var getOptions = function() {
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

var getReportType = function() {
  return {
    bug: 'bug',
    feedback: 'feedback',
    question: 'question'
  };
};

/**
 * BugReporting module
 * @exports BugReporting
 */
var BugReporting = function() {};

BugReporting.invocationEvents = getInvocationEvents();
/**
 * @deprecated
 */
BugReporting.invocationOptions = getOptions();
BugReporting.option = getOptions();
BugReporting.invocationModes = getInvocationModes();
/**
 * @deprecated
 */
BugReporting.extendedBugReportMode = getExtendedBugReportMode();
BugReporting.reportType = getReportType();

/**
 * Enables or disables all bug reporting functionalities.
 * @param {boolean} isEnabled
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
BugReporting.setEnabled = function(isEnabled, success, error) {
  exec(success, error, 'IBGPlugin', 'setBugReportingEnabled', [isEnabled]);
};

/**
 * Sets report type either bug, feedback or both.
 * @param {enum} reportType Array of reportType
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
BugReporting.setReportTypes = function(reportTypes, success, error) {
  var validatedTypes = [];
  for (let i = 0; i < reportTypes.length; i++) {
    var validatedType = getReportType()[reportTypes[i]];
    if (validatedType) {
      validatedTypes.push(validatedType);
    }
  }
  if (validatedTypes !== undefined || validatedTypes.length != 0) {
    exec(success, error, 'IBGPlugin', 'setReportTypes', [reportTypes]);
  }
};

/**
 * Shows report view with specified options.
 * @param {enum} reportType reportType
 * @param {array} options array of Invocation options
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
BugReporting.showWithOptions = function(reportType, options, success, error) {
  if (reportType && options) {
    exec(
      success,
      error,
      'IBGPlugin',
      'showBugReportingWithReportTypeAndOptions',
      [reportType, options]
    );
  }
};

/**
 * @deprecated use {@link BugReporting.setOptions}
 * Sets the invocation options.
 * Default is set by `Instabug.startWithToken`.
 * @param {enum} options Array of Option
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
BugReporting.setInvocationOptions = function(options, success, error) {
  var i;
  var validatedOptions = [];
  for (i = 0; i < options.length; i++) {
    var validatedOption = getOptions()[options[i]];
    if (validatedOption) {
      validatedOptions.push(validatedOption);
    }
  }
  if (validatedOptions !== undefined || validatedOptions.length != 0) {
    exec(success, error, 'IBGPlugin', 'setInvocationOptions', [
      validatedOptions
    ]);
  } else {
    console.log(
      'Could not change invocation option - "' +
        validatedOptions +
        '" is empty.'
    );
  }
};

/**
 * Sets the invocation options.
 * Default is set by `Instabug.startWithToken`.
 * @param {enum} options Array of Option
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
BugReporting.setOptions = function(options, success, error) {
  BugReporting.setInvocationOptions(options, success, error);
}

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
    console.log(
      'Could not change invocation event - "' + event + '" is not valid.'
    );
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
 *
 * @param {enum} extendedBugReportMode ExtendedBugReportMode
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
BugReporting.setExtendedBugReportMode = function(
  extendedBugReportMode,
  success,
  error
) {
  var validatedExtendedBugReportMode = getExtendedBugReportMode()[
    extendedBugReportMode
  ];

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

});
