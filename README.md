# Instabug Cordova Plugin

The purpose of this plugin is to simplify the process of integrating the Instabug SDK in a hybrid application, as well as to provide an interface to interact with the SDK through JavaScript.

Instabug is a bug reporting and in-app feedback tool that provides your testers and users with easy “shake to send” feedback from within the app, to [report bugs and issues easily](https://instabug.com/bug-reporting). You’ll get attached screenshots, screen recordings, annotations, network logs to help you take insightful decisions. You’ll also receive all the device details, environment snapshots and bug reproduction steps so that you can fix bugs and iterate faster.

For more info, visit [Instabug.com](https://instabug.com).

## Installation

### Cordova:

```
cordova plugin add instabug-cordova
```

### Ionic:

```
ionic cordova plugin add instabug-cordova
```

## Integration Steps

- Add the following snippet to your `index.js` file inside `onDeviceReady` function:   

```js
var Instabug = cordova.require("instabug-cordova.Instabug");
var BugReporting = cordova.require("instabug-cordova.BugReporting");

Instabug.start(
    "YOUR_CORDOVA_TOKEN",
    [BugReporting.invocationEvents.button],
    () => console.log("Instabug initialized."),
    (error) => console.log("Instabug could not be initialized - " + error)
);
```

- Replace `YOUR_CORDOVA_TOKEN` with your application token.

> :warning:  If you're updating the SDK from versions prior to v11, please check our [migration guide](https://docs.instabug.com/docs/cordova-migration-guide).

## Features Not Yet Supported
- User steps.
- Repro steps.
- Network logging.
- View hierarchy.
- Crash reporting.
- Push notification for in-app messaging.

## License

This software is released under the <a href="http://opensource.org/licenses/Apache-2.0">Apache 2.0 License</a>.

© 2016 Instabug. All rights reserved.
