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

Instabug.invoke = function (mode, success, error) {
	var validatedMode = getInvocationModes()[mode];

	if (validatedMode) {
		exec(success, error, 'IBGPlugin', 'invoke', [ validatedMode ]);
	} else {
		exec(success, error, 'IBGPlugin', 'invoke', []);
		console.log('Could not apply mode to invocation - "' + mode + '" is not valid.');
	}
};

Instabug.showIntro = function(success, error) {
	exec(success, error, 'IBGPlugin', 'showIntroDialog', []);
};

Instabug.setPrimaryColor = function (colorInteger, success, error) {
	exec(success, error, 'IBGPlugin', 'setPrimaryColor', [ colorInteger ]);
};

Instabug.setUserEmail = function (email, success, error) {
	exec(success, error, 'IBGPlugin', 'setUserEmail', [ email ]);
};

Instabug.setUserName = function (name, success, error) {
	exec(success, error, 'IBGPlugin', 'setUserName', [ name ]);
};

Instabug.setUserData = function (data, success, error) {
	exec(success, error, 'IBGPlugin', 'setUserData', [ data ]);
};

Instabug.addFile = function (filePath, success, error) {
	exec(success, error, 'IBGPlugin', 'addFile', [ filePath ]);
}

Instabug.clearFileAttachments = function (success, error) {
	exec(success, error, 'IBGPlugin', 'clearFileAttachments');
}

Instabug.addLog = function (content, success, error) {
	exec(success, error, 'IBGPlugin', 'addLog', [ content ]);
};

Instabug.logVerbose = function (content, success, error) {
	exec(success, error, 'IBGPlugin', 'logVerbose', [ content ]);
};

Instabug.logDebug = function (content, success, error) {
	exec(success, error, 'IBGPlugin', 'logDebug', [ content ]);
};

Instabug.logInfo = function (content, success, error) {
	exec(success, error, 'IBGPlugin', 'logInfo', [ content ]);
};

Instabug.logWarn = function (content, success, error) {
	exec(success, error, 'IBGPlugin', 'logWarn', [ content ]);
};

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

Instabug.setLocale = function (locale, success, error) {
	var validatedLocale = getLocales()[locale];

	if (validatedLocale) {
		exec(success, error, 'IBGPlugin', 'setLocale', [ validatedLocale ]);
	} else {
		console.log('Could not set locale - "' + locale + '" is not valid.');
	}
};

module.exports = Instabug;
