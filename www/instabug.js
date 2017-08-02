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
        turkish: 'turkish',
        czech: 'czech'
    };
};

var Instabug = function () {
};

Instabug.activate = function (options, event, success, error) {
    var validatedEvent = getInvocationEvents()[event];

    if (validatedEvent) {
        exec(success, error, 'IBGPlugin', 'activate', [validatedEvent, options]);
    } else {
        console.log('Could not activate Instabug - invocation event "' + event + '" is not valid.');
    }
};

Instabug.invoke = function (mode, success, error) {
    var validatedMode = getInvocationModes()[mode];

    if (validatedMode) {
        exec(success, error, 'IBGPlugin', 'invoke', [validatedMode]);
    } else {
        exec(success, error, 'IBGPlugin', 'invoke', []);
        console.log('Could not apply mode to invocation - "' + mode + '" is not valid.');
    }
};

Instabug.showIntro = function (success, error) {
    exec(success, error, 'IBGPlugin', 'showIntroDialog', []);
};

Instabug.setPrimaryColor = function (colorInteger, success, error) {
    exec(success, error, 'IBGPlugin', 'setPrimaryColor', [colorInteger]);
};

Instabug.setUserEmail = function (email, success, error) {
    exec(success, error, 'IBGPlugin', 'setUserEmail', [email]);
};

Instabug.setUserName = function (name, success, error) {
    exec(success, error, 'IBGPlugin', 'setUserName', [name]);
};

Instabug.setUserData = function (data, success, error) {
    exec(success, error, 'IBGPlugin', 'setUserData', [data]);
};

Instabug.addFile = function (filePath, success, error) {
    exec(success, error, 'IBGPlugin', 'addFile', [filePath]);
}

Instabug.addLog = function (content, success, error) {
    exec(success, error, 'IBGPlugin', 'addLog', [content]);
};

Instabug.clearLog = function (success, error) {
    exec(success, error, 'IBGPlugin', 'clearLog', []);
};

Instabug.changeInvocationEvent = function (event, success, error) {
    var validatedEvent = getInvocationEvents()[event];

    if (validatedEvent) {
        exec(success, error, 'IBGPlugin', 'changeInvocationEvent', [validatedEvent]);
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

Instabug.setDebugEnabled = function (isDebugEnabled, success, error) {
    exec(success, error, 'IBGPlugin', 'setDebugEnabled', [isDebugEnabled]);
    if(success) {
        console.log("setting debug enabled to " + isDebugEnabled);
    } else if(error) {
        console.log("setting debug enabled not successful");
    }
};

Instabug.setLocale = function (locale, success, error) {
    var validatedLocale = getLocales()[locale];

    if (validatedLocale) {
        exec(success, error, 'IBGPlugin', 'setLocale', [validatedLocale]);
    } else {
        console.log('Could not set locale - "' + locale + '" is not valid.');
    }
};

module.exports = Instabug;
