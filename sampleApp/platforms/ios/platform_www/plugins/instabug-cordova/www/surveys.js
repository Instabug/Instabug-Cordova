cordova.define("instabug-cordova.Surveys", function(require, exports, module) {
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
 * @param {function():void} success 
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
 * @param {function():void} success 
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
 * @param {function():void} success 
 * @param {function():void} error 
 */
Surveys.showSurveyIfAvailable = function (success, error) {
    exec(success, error, 'IBGPlugin', 'showSurveyIfAvailable', []);
};

/**
 * @summary Sets a block of code to be executed just before the survey's UI is presented.
 * This block is executed on the UI thread. Could be used for performing any UI changes before
 * the survey's UI is shown.
 * @param {function():void} success 
 * @param {function():void} error 
 */
Surveys.setOnShowHandler = function (success, error) {
    exec(success, error, 'IBGPlugin', 'willShowSurveyHandler', []);
};

/**
 * @summary Sets a block of code to be executed right after the survey's UI is dismissed.
 * This block is executed on the UI thread. Could be used for performing any UI
 * changes after the survey's UI is dismissed.
 * @param {function():void} success 
 * @param {function():void} error 
 */
Surveys.setOnDismissHandler = function (success, error) {
    exec(success, error, 'IBGPlugin', 'didDismissSurveyHandler', []);
};

/**
 * Shows survey with a specific token.
 * Does nothing if there are no available surveys with that specific token.
 * Answered and cancelled surveys won't show up again.
 * @param {string} surveyToken A String with a survey token.
 * @param {function():void} success 
 * @param {function(string):void} error 
 */
Surveys.showSurveyWithToken = function (surveyToken, success, error) {
    exec(success, error, 'IBGPlugin', 'showSurveyWithToken', [surveyToken]);
};

/**
 * Sets a threshold for numbers of sessions and another for number of days
 * required before a survey, that has been dismissed once, would show again.
 * @param {number} sessionsCount Number of sessions required to be
 *                initialized before a dismissed survey can be shown again.
 * @param {number} daysCount Number of days required to pass before a
 *                dismissed survey can be shown again.
 * @param {function():void} success 
 * @param {function(string):void} error 
 */
Surveys.setThresholdForReshowingSurveyAfterDismiss = function (sessionsCount, daysCount, success, error) {
    exec(success, error, 'IBGPlugin', 'setThresholdForReshowingSurveyAfterDismiss', [sessionsCount, daysCount]);
};

/**
 * Returns true if the survey with a specific token was answered before.
 * Will return false if the token does not exist or if the survey was not answered before.
 * @param {string} surveyToken a string with a survey token.
 * @param {function(boolean):void} success 
 * @param {function(string):void} error 
 */
Surveys.hasRespondedToSurveyWithToken = function (surveyToken, success, error) {
    exec(success, error, 'IBGPlugin', 'hasRespondedToSurveyWithToken', [surveyToken]);
};

/**
 * Returns an array containing the available surveys.
 * @param {function(Object):void} success 
 * @param {function():void} error 
 */
Surveys.getAvailableSurveys = function (success, error) {
    exec(success, error, 'IBGPlugin', 'getAvailableSurveys', []);
};

/**
 * Setting an option for all the surveys to show a welcome screen before
 * the user starts taking the survey.
 * @param {boolean} shouldShowWelcomeScreen  A boolean for setting whether the
 *                                welcome screen should show.
 * @param {function():void} success 
 * @param {function(string):void} error 
 */
Surveys.setShouldShowSurveysWelcomeScreen = function (shouldShowWelcomeScreen, success, error) {
    exec(success, error, 'IBGPlugin', 'setShouldShowSurveysWelcomeScreen', [shouldShowWelcomeScreen]);
};

module.exports = Surveys;

});
