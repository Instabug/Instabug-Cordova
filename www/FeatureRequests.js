/*
 * Instabug Feature Requests module.
 * @file featureRequests.js
 */

var exec = require('cordova/exec');

/**
 * Types of actions on feature requests.
 * @readonly
 * @enum {string}
 */
var getActionTypes = function () {
    return {
        requestNewFeature: 'requestNewFeature',
        addCommentToFeature: 'addCommentToFeature'
    };
};

/**
 * FeatureRequests module
 * @exports FeatureRequests
 */
var FeatureRequests = function() {};

FeatureRequests.actionTypes = getActionTypes();

/**
 * Shows the UI for feature requests list.
 * @param {function(void):void} success 
 * @param {function(void):void} error 
 */
FeatureRequests.show = function (success, error) {
    exec(success, error, 'IBGPlugin', 'showFeatureRequests', []);
};

/**
 * Sets whether users are required to enter an email address or not when
 * sending reports.
 * Defaults to YES.
 * @param {boolean} isRequired indicates if email field is required
 * @param {enum} actionTypes array of ActionType 
 * @param {function(void):void} success 
 * @param {function(string):void} error 
 */
FeatureRequests.setEmailFieldRequired = function (isRequired, actionTypes, success, error) {
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


module.exports = FeatureRequests;
