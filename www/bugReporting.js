/*
 * Instabug Bug Reporting module
 */

var exec = require('cordova/exec');


/**
 * The options used upon invoking the SDK
 * @readonly
 * @enum {number}
 */
var getInvocationOptions = function () {
    return {
        emailFieldHidden: 'emailFieldHidden',
        emailFieldOptional: 'emailFieldOptional',
        commentFieldRequired: 'commentFieldRequired',
        disablePostSendingDialog: 'disablePostSendingDialog'
    };
}; 


/**
 * BugReporting module
 * @exports BugReporting
 */
var BugReporting = function () {
};

BugReporting.invocationOptions = getInvocationOptions();

/**
 * Sets the invocation options.
 * Default is set by `Instabug.startWithToken`.
 * @param {options} invocationOptions Array of invocation options
 * @param {success} success callback on function success
 * @param {error} error callback on function error
 */
BugReporting.setInvocationOptions = function (options, success, error) {
    alert('in here')
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


  module.exports = BugReporting;