var exec = require('cordova/exec');

var getInvocationEvents = function () {
	return {
		shake: 'shake',
		button: 'button',
		screenshot: 'screenshot',
		swipe: 'swipe',
		pan: 'pan',
		none: 'none'
	};
};

var getInvocationModes = function () {
	return {
		bug: 'bug',
		feedback: 'feedback',
		na: 'na'
	};
};

var getLocales = function () {
	return {
		arabic: 'arabic',
		chineseSimplified: 'chineseSimplified',
		chineseTraditional: 'chineseTraditional',
		english: 'english',
		french: 'french',
		german: 'german',
		italian: 'italian',
		japanese: 'japanese',
		korean: 'korean',
		polish: 'polish',
		portugueseBrazil: 'portugueseBrazil',
		russian: 'russian',
		spanish: 'spanish',
		swedish: 'swedish',
		turkish: 'turkish'
	};
};

var Instabug = function () {};

Instabug.activate = function (token, event, options, success, error) {
	var validatedEvent = getInvocationEvents()[event];

	if (validatedEvent) {
		exec(success, error, 'IBPlugin', 'activate', [ token, validatedEvent, options ]);
	} else {
		console.log('Could not activate Instabug - invocation event "' + event + '" is not valid.');
	}
};

Instabug.invoke = function (mode, success, error) {
	var validatedMode = getInvocationModes()[mode];

	if (validatedMode) {
		exec(success, error, 'IBPlugin', 'invoke', [ validatedMode ]);
	} else {
		exec(success, error, 'IBPlugin', 'invoke', []);
		console.log('Could not apply mode to invocation - "' + mode + '" is not valid.');
	}
};

Instabug.showIntro = function(success, error) {
	exec(success, error, 'IBPlugin', 'showIntroDialog', []);
};

Instabug.setPrimaryColor = function (hexColor, success, error) {
	exec(success, error, 'IBPlugin', 'setPrimaryColor', [ hexColor ]);
};

Instabug.setUserEmail = function (email, success, error) {
	exec(success, error, 'IBPlugin', 'setUserEmail', [ email ]);
};

Instabug.setUserName = function (name, success, error) {
	exec(success, error, 'IBPlugin', 'setUserName', [ name ]);
};

Instabug.setUserData = function (data, success, error) {
	exec(success, error, 'IBPlugin', 'setUserData', [ data ]);
};

Instabug.addFile = function (filePath, success, error) {
	exec(success, error, 'IBPlugin', 'addFile', [ filePath ]);
}

Instabug.addLog = function (content, success, error) {
	exec(success, error, 'IBPlugin', 'addLog', [ content ]);
};

Instabug.clearLog = function (success, error) {
	exec(success, error, 'IBPlugin', 'clearLog', []);
};

Instabug.changeInvocationEvent = function (event, success, error) {
	var validatedEvent = getInvocationEvents()[event];

	if (validatedEvent) {
		exec(success, error, 'IBPlugin', 'changeInvocationEvent', [ validatedEvent ]);
	} else {
		console.log('Could not change invocation event - "' + event + '" is not valid.');
	}
};

Instabug.disable = function (success, error) {
	exec(success, error, 'IBPlugin', 'disable', []);
};

Instabug.enable = function (success, error) {
	exec(success, error, 'IBPlugin', 'enable', []);
};

Instabug.isEnabled = function (success, error) {
	exec(success, error, 'IBPlugin', 'isEnabled', []);
};

Instabug.isInvoked = function (success, error) {
	exec(success, error, 'IBPlugin', 'isInvoked', []);
};

Instabug.isDebugEnabled = function (success, error) {
	exec(success, error, 'IBPlugin', 'isDebugEnabled', []);
};

Instabug.setLocale = function (locale, success, error) {
	var validatedLocale = getLocales()[locale];

	if (validatedLocale) {
		exec(success, error, 'IBPlugin', 'setLocale', [ validatedLocale ]);
	} else {
		console.log('Could not set locale - "' + locale + '" is not valid.');
	}
};

module.exports = Instabug;
