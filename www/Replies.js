/*
 * Instabug Replies module.
 * @file replies.js
 */

var exec = require('cordova/exec');

/**
 * Replies module
 * @exports Replies
 */
var Replies = function() {};

/**
 * Enables or disables all replies functionalities.
 * @param {boolean} isEnabled
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
Replies.setEnabled = function(isEnabled, success, error) {
  exec(success, error, 'IBGPlugin', 'setRepliesEnabled', [isEnabled]);
};

/**
 * Shows replies.
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
Replies.show = function(success, error) {
    exec(success, error, 'IBGPlugin', 'showReplies');
}

/**
 * Calls success callback if chats exist.
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
Replies.hasChats = function(success, error) {
    exec(success, error, 'IBGPlugin', 'hasChats');
}

/**
 * Returns the number of unread replies for the user.
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
Replies.getUnreadRepliesCount = function(success, error) {
    exec(success, error, 'IBGPlugin', 'getUnreadRepliesCount');
}

/**
 * Enables in app notifications for any new reply received.
 * @param {boolean} isEnabled
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
Replies.setInAppNotificationEnabled = function (isEnabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setChatNotificationEnabled', [isEnabled]);
};

/**
 * Enables/disables the use of push notifications in the SDK.
 * Defaults to YES.
 * @param {boolean} isEnabled
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
Replies.setPushNotificationsEnabled = function (isEnabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setPushNotificationsEnabled', [isEnabled]);
};

module.exports = Replies;