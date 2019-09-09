cordova.define("instabug-cordova.Chats", function(require, exports, module) {
/*
 * Instabug Chats module.
 * @file chats.js
 */

var exec = require('cordova/exec');

/**
 * Chats module
 * @exports Chats
 */
var Chats = function() {};

/**
 * @deprecated
 * Enables or disables all chats functionalities.
 * @param {boolean} isEnabled
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
Chats.setEnabled = function(isEnabled, success, error) {
  exec(success, error, 'IBGPlugin', 'setChatsEnabled', [isEnabled]);
};

/**
 * @deprecated
 * Shows chats with the option of chats list or new chat.
 * @param {boolean} withChatsList
 * @param {function(void):void} success callback on function success
 * @param {function(void):void} error callback on function error
 */
Chats.show = function(success, error) {
    exec(success, error, 'IBGPlugin', 'showChats');
}

module.exports = Chats;

});
