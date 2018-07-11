/*
 * Instabug Bug Reporting module.
 * @file bugReporting.js
 */

var exec = require('cordova/exec');

/**
 * The options used upon invoking the SDK
 * @readonly
 * @enum {string} InvocationOptions
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
 * @enum {string} InvocationModes
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
 * BugReporting module
 * @exports BugReporting
 */
var BugReporting = function() {};

BugReporting.invocationOptions = getInvocationOptions();
BugReporting.invocationModes = getInvocationModes();

/**
 * Sets the invocation options.
 * Default is set by `Instabug.startWithToken`.
 * @param {enum} invocationOptions Array of InvocationOptions
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
 * @param {enum} invocationOptions Array of InvocationOptions
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


  module.exports = BugReporting;