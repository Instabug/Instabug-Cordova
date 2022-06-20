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
BugReporting.option = getOptions();
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
  for (var i = 0; i < reportTypes.length; i++) {
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
 * Enables or disables view hierarchy.
 * 
 * @param {boolean} enabled
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
BugReporting.setViewHierarchyEnabled = function (enabled, success, error) {
  exec(success, error, 'IBGPlugin', 'setViewHierarchyEnabled', [enabled]);
};

/**
 * Sets the invocation options.
 * Default is set by `Instabug.start`.
 * @param {enum} options Array of Option
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
BugReporting.setOptions = function(options, success, error) {
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

/**
 * Sets the default edge and offset from the top at which the floating button
 * will be shown. Different orientations are already handled.
 * 
 * @param {keyof Instabug.floatingButtonEdge} edge - The position of the edge, the default is right.
 * @param {number} offset From the top edge, default is left.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
BugReporting.setFloatingButtonEdge = function(edge, offset, success, error) {
  exec(success, error, 'IBGPlugin', 'setFloatingButtonEdge', [edge, offset]);
};

/**
 * Sets the threshold value of the shake gesture for iPhone/iPod Touch
 * Default for iPhone is 2.5.
 * @param {number} threshold Threshold for iPhone.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
BugReporting.setShakingThresholdForiPhone = function(threshold, success, error) {
  exec(success, error, 'IBGPlugin', 'setShakingThresholdForiPhone', [threshold]);
};

/**
 * Sets the threshold value of the shake gesture for iPad.
 * Default for iPad is 0.6.
 * @param {number} threshold Threshold for iPad.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
BugReporting.setShakingThresholdForiPad = function(threshold, success, error) {
  exec(success, error, 'IBGPlugin', 'setShakingThresholdForiPad', [threshold]);
};

/**
 * Sets the threshold value of the shake gesture for android devices.
 * Default for android is an integer value equals 350.
 * you could increase the shaking difficulty level by
 * increasing the `350` value and vice versa
 * @param {number} threshold Threshold for android devices.
 * @param {function} success callback on function success
 * @param {function(string):void} error callback on function error
 */
BugReporting.setShakingThresholdForAndroid = function(threshold, success, error) {
  exec(success, error, 'IBGPlugin', 'setShakingThreshold', [threshold]);
};


module.exports = BugReporting;
