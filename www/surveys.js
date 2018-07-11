/*
 * Instabug Surveys module.
 * @file surveys.js
 */

var exec = require('cordova/exec');


/**
 * Surveys module
 * @exports Surveys
 */
var Surveys = function() {};

/**
 * Sets whether auto surveys showing are enabled or not.
 * @param {boolean} autoShowingSurveysEnabled  A boolean for whether to auto show a survey
 * @param {function(void):void} success 
 * @param {function(string)} error 
 */
Surveys.setAutoShowingEnabled = function (autoShowingSurveysEnabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setAutoShowingSurveysEnabled', [autoShowingSurveysEnabled]);
};

/**
 * @summary Sets whether surveys are enabled or not.
 * If you disable surveys on the SDK but still have active surveys on your Instabug dashboard,
 * those surveys are still going to be sent to the device, but are not going to be
 * shown automatically.
 * To manually display any available surveys, call `Instabug.showSurveyIfAvailable()`.
 * Defaults to `true`.
 * @param {boolean} isEnabled 
 * @param {function(void):void} success 
 * @param {function(string):void} error 
 */
Surveys.setEnabled = function (isEnabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setSurveysEnabled', [isEnabled]);
};

/**
 * @summary Shows one of the surveys that were not shown before, that also have conditions
 * that match the current device/user.
 * Does nothing if there are no available surveys or if a survey has already been shown
 * in the current session.
 * @param {function(void):void} success 
 * @param {function(void):void} error 
 */
Surveys.showSurveyIfAvailable = function (success, error) {
    exec(success, error, 'IBGPlugin', 'showSurveyIfAvailable', []);
};




module.exports = Surveys;
