var exec = require('cordova/exec');

var getInvocationEvents = function () {
	return {
		shake: 'shake',
		button: 'button',
		screenshot: 'screenshot',
		swipe: 'swipe',
		none: 'none'
	};
};

var getInvocationModes = function () {
	return {
		chat: 'chat',
		chats: 'chats',
		bug: 'bug',
		feedback: 'feedback',
		options: 'options'
	};
};

var getLocales = function () {
	return {
		arabic: 'arabic',
		chineseSimplified: 'chineseSimplified',
		chineseTraditional: 'chineseTraditional',
		czech:'czech',
		danish:'danish',
		dutch:'dutch',
		english: 'english',
		french: 'french',
		german: 'german',
		italian: 'italian',
		japanese: 'japanese',
		korean: 'korean',
		norwegian: 'norwegian',
		polish: 'polish',
		portuguese: 'portuguese',
		portugueseBrazil: 'portugueseBrazil',
		russian: 'russian',
		slovak: 'slovak',
		spanish: 'spanish',
		swedish: 'swedish',
		turkish: 'turkish',
	};
};

var Instabug = function () {};

Instabug.activate = function (token, event, options, success, error) {
	var validatedEvent = getInvocationEvents()[event];

	if (validatedEvent) {
		exec(success, error, 'IBGPlugin', 'activate', [ token, validatedEvent, options ]);
	} else {
		console.log('Could not activate Instabug - invocation event "' + event + '" is not valid.');
	}
};

/**
* Invokes the SDK manually with the default invocation mode.
* Shows a view that asks the user whether they want to start a chat, report a problem or suggest an improvement.
* @param {string} mode Specifies which mode the SDK is going to start with.
*/
Instabug.invoke = function (mode, success, error) {
	var validatedMode = getInvocationModes()[mode];

	if (validatedMode) {
		exec(success, error, 'IBGPlugin', 'invoke', [ validatedMode ]);
	} else {
		exec(success, error, 'IBGPlugin', 'invoke', []);
		console.log('Could not apply mode to invocation - "' + mode + '" is not valid.');
	}
};

/**
* Present a view that educates the user on how to invoke the SDK with the currently set invocation event.
* Does nothing if invocation event is set to anything other than IBGInvocationEventShake or IBGInvocationEventScreenshot.
*/
Instabug.showIntro = function(success, error) {
	exec(success, error, 'IBGPlugin', 'showIntroDialog', []);
};

/**
* Sets the primary color of the SDK's UI.
* Sets the color of UI elements indicating interactivity or call to action.
* @param {string} colorInteger A color to set the UI elements of the SDK to.
*/
Instabug.setPrimaryColor = function (colorInteger, success, error) {
	exec(success, error, 'IBGPlugin', 'setPrimaryColor', [ colorInteger ]);
};

/**
* Sets the default value of the user's email and hides the email field
* Defaults to an empty string.
* @param {string} userName An email address to be set as the user's email.
*/
Instabug.setUserEmail = function (userName, success, error) {
	exec(success, error, 'IBGPlugin', 'setUserEmail', [ userName ]);
};

/**
* Sets the default value of the user's name to be included with all reports.
* Defaults to an empty string.
* @param {string} userName An email address to be set as the user's email.
*/
Instabug.setUserName = function (userName, success, error) {
	exec(success, error, 'IBGPlugin', 'setUserName', [ userName ]);
};

/**
* Attaches user data to each report being sent.
* Each call to this method overrides the user data to be attached.
* Maximum size of the string is 1,000 characters.
* @param {string} userData A string to be attached to each report, with a maximum size of 1,000 characters.
*/
Instabug.setUserData = function (userData, success, error) {
	exec(success, error, 'IBGPlugin', 'setUserData', [ userData ]);
};

/**
* Add file to attached files with each report being sent.
*
* A new copy of the file at filePath will be attached with each bug report being sent. The file is only copied
* at the time of sending the report, so you could safely call this API whenever the file is available on disk, and the copy
* attached to your bug reports will always contain that latest changes at the time of sending the report.
*
* Each call to this method adds the file to the files attached, until a maximum of 3 then it overrides the first file. 
* The file has to be available locally at the provided path when the report is being sent.
*
* @param {string} filePath Path to a file that's going to be attached to each report.
*/
Instabug.addFile = function (filePath, success, error) {
	exec(success, error, 'IBGPlugin', 'addFile', [ filePath ]);
}

/**
* Clear list of files to be attached with each report.
*
* This method doesn't delete any files from the file system. It will just removes them for the list of files
* to be attached with each report.
*/
Instabug.clearFileAttachments = function (success, error) {
	exec(success, error, 'IBGPlugin', 'clearFileAttachments');
}

/**
* Adds custom logs that will be sent with each report. Logs are added with the debug log level.
*
* @param {string} content Message to be logged.
*/
Instabug.addLog = function (content, success, error) {
	exec(success, error, 'IBGPlugin', 'addLog', [ content ]);
};

/**
* Adds custom logs with the verbose log level. Logs will be sent with each report.
*
* @param {string} content Message to be logged.
*/
Instabug.logVerbose = function (content, success, error) {
	exec(success, error, 'IBGPlugin', 'logVerbose', [ content ]);
};

/**
* Adds custom logs with the debug log level. Logs will be sent with each report.
*
* @param {string} content Message to be logged.
*/
Instabug.logDebug = function (content, success, error) {
	exec(success, error, 'IBGPlugin', 'logDebug', [ content ]);
};

/**
* Adds custom logs with the info log level. Logs will be sent with each report.
*
* @param {string} content Message to be logged.
*/
Instabug.logInfo = function (content, success, error) {
	exec(success, error, 'IBGPlugin', 'logInfo', [ content ]);
};

/**
* Adds custom logs with the warn log level. Logs will be sent with each report.
*
* @param {string} content Message to be logged.
*/
Instabug.logWarn = function (content, success, error) {
	exec(success, error, 'IBGPlugin', 'logWarn', [ content ]);
};

/**
* Adds custom logs with the error log level. Logs will be sent with each report.
*
* @param {string} content Message to be logged.
*/
Instabug.logError = function (content, success, error) {
	exec(success, error, 'IBGPlugin', 'logError', [ content ]);
};

Instabug.clearLog = function (success, error) {
	exec(success, error, 'IBGPlugin', 'clearLog', []);
};

Instabug.changeInvocationEvent = function (event, success, error) {
	var validatedEvent = getInvocationEvents()[event];

	if (validatedEvent) {
		exec(success, error, 'IBGPlugin', 'changeInvocationEvent', [ validatedEvent ]);
	} else {
		console.log('Could not change invocation event - "' + event + '" is not valid.');
	}
};

Instabug.disable = function (success, error) {
	exec(success, error, 'IBGPlugin', 'disable', []);
};

Instabug.enable = function (success, error) {
	exec(success, error, 'IBGPlugin', 'enable', []);
};

Instabug.isEnabled = function (success, error) {
	exec(success, error, 'IBGPlugin', 'isEnabled', []);
};

Instabug.isInvoked = function (success, error) {
	exec(success, error, 'IBGPlugin', 'isInvoked', []);
};

Instabug.isDebugEnabled = function (success, error) {
	exec(success, error, 'IBGPlugin', 'isDebugEnabled', []);
};

/**
* Sets the SDK's locale.
*
* Use to change the SDK's UI to different language.
* Defaults to the device's current locale.
* 
* @param {string} locale A locale to set the SDK to.
*/
Instabug.setLocale = function (locale, success, error) {
	var validatedLocale = getLocales()[locale];

	if (validatedLocale) {
		exec(success, error, 'IBGPlugin', 'setLocale', [ validatedLocale ]);
	} else {
		console.log('Could not set locale - "' + locale + '" is not valid.');
	}
};

/**
* Sets an array of report categories to be shown for users to select from before reporting a bug or sending 
* feedback.
*
* Use this method to give users a list of choices of categories their bug report or feedback might be related
* to. Selected category will be shown as a tag on your dashboard.
*
* @param {array} titles Array of titles to be shown in the list.
* @param {array} names Array of names of icons to be shown along with titles.
*/
Instabug.setReportCategoriesWithTitlesAndIcons = function (titles, iconNames, success, error) {
	exec(success, error, 'IBGPlugin', 'setReportCategoriesWithTitlesAndIcons', [ titles, iconNames ]);
};

/**
* Set custom user attributes that are going to be sent with each feedback, bug or crash.

@param {string} value User attribute value.
@param {string} key User attribute key.
*/
Instabug.setUserAttributes = function (value, key, success, error) {
	exec(success, error, 'IBGPlugin', 'setUserAttributes', [ value, key ]);
};

/**
* Returns the user attribute associated with a given key.
*
* @param {string} key The key for which to return the corresponding value..
* 
* @return {string} returnedValue The value associated with aKey, or nil if no value is associated with aKey.
*/
Instabug.userAttributeForKey = function (key, returnedValue, error) {
	exec(returnedValue, error, 'IBGPlugin', 'userAttributeForKey', [ key ]);
};

/**
* Removes a given key and its associated value from user attributes.
*
*Does nothing if aKey does not exist.
*
*@param {string} key The key to remove.
*/
Instabug.removeUserAttributeForKey = function (key, success, error) {
	exec(success, error, 'IBGPlugin', 'removeUserAttributeForKey', [ key ]);
};

/**
* Appends a set of tags to previously added tags of reported feedback, bug or crash.
*
*@param {array} tags An array of tags to append to current tags.
*/
Instabug.appendTags = function (tags, success, error) {
	exec(success, error, 'IBGPlugin', 'appendTags', [ tags ]);
};

/**
* Manually removes all tags of reported feedback, bug or crash.
*/
Instabug.resetTags = function (success, error) {
	exec(success, error, 'IBGPlugin', 'resetTags', []);
};

/**
* Gets all tags of reported feedback, bug or crash.
*
* @return Returns array of tages being added during the session.
*/
Instabug.getTags = function (tags, returnedTages, error) {
	exec(returnedTages, error, 'IBGPlugin', 'getTags', []);
};

/**
* Sets the user email and name for all sent reports. Also hides the email field from the reporting UI.

* @param {string} email Email address to be set as the user's email.
* @param {string} name Name of the user to be set.
*/
Instabug.identifyUserWithEmailAndName = function (email, name, success, error) {
	exec(success, error, 'IBGPlugin', 'identifyUserWithEmailAndName', [ email, name ]);
};

/**
* Sets the default value of the user's email to nil and show email field and remove user name from all reports
* 
* This method also resets all chats currently on the device and removes any set user attributes.
*/
Instabug.logout = function (success, error) {
	exec(success, error, 'IBGPlugin', 'logout', []);
};

/**
* Logs a user event that happens through the lifecycle of the application.
*
* Logged user events are going to be sent with each report, as well as at the end of a session.
*
* @param {string} name Event name.
*/
 Instabug.logUserEventWithName = function (name, success, error) {
	exec(success, error, 'IBGPlugin', 'logUserEventWithName', [ name ]);
};

/**
* Logs a user event that happens through the lifecycle of the application.
*
* Logged user events are going to be sent with each report, as well as at the end of a session.
*
* @param {string} name Event name.
* @param {object} params An optional dictionary or parameters to be associated with the event.
*/
Instabug.logUserEventWithNameAndParams = function (name, params, success, error) {
	exec(success, error, 'IBGPlugin', 'logUserEventWithNameAndParams', [ name, params ]);
};

module.exports = Instabug;
